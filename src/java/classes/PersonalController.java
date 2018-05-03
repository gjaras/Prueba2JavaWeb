package classes;

import classes.util.JsfUtil;
import classes.util.PaginationHelper;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@Named("personalController")
@SessionScoped
public class PersonalController implements Serializable {

    private Personal current;
    private DataModel items = null;
    @EJB
    private classes.PersonalFacade ejbFacade;
    @EJB
    private classes.ComunaFacade ejbComunaFacade;
    @EJB
    private classes.ProfesionFacade ejbProfesionFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private String searchPK;

    public PersonalController() {
    }

    public Personal getSelected() {
        if (current == null) {
            current = new Personal();
            selectedItemIndex = -1;
        }
        return current;
    }

    private PersonalFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }
    
    public String renderSearchForm() {
        return "SearchEdit";
    }
    
    public String renderDeleteSearchForm() {
        return "SearchDelete";
    }

    public String prepareView() {
        current = (Personal) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String prepareCreate() {
        if(ejbComunaFacade.count() < 1 || ejbProfesionFacade.count() < 1){
            if(ejbComunaFacade.count() < 1){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NoComunaErrorOccured"));
            }
            if(ejbProfesionFacade.count() < 1){
                JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("NoProfesionErrorOccured"));
            }
            return null;
        }
        current = new Personal();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonalCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Personal) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }
    
    public String searchEdit() {
        current = getPersonal(getSearchPK());
        if(current == null){
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersonalNotFoundError"));
            return null;
        }
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonalFound"));
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonalUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Personal) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }
    
    public String seekAndDestroy() {
        current = getPersonal(getSearchPK());
        if(current == null){
            JsfUtil.addErrorMessage(ResourceBundle.getBundle("/Bundle").getString("PersonalNotFoundError"));
            return null;
        }
        performDestroy();
        recreatePagination();
        recreateModel();
        JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonalFoundAndDestroyed"));
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonalDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Personal getPersonal(java.lang.String id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Personal.class)
    public static class PersonalControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonalController controller = (PersonalController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personalController");
            return controller.getPersonal(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Personal) {
                Personal o = (Personal) object;
                return getStringKey(o.getRut());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Personal.class.getName());
            }
        }

    }

    /**
     * @return the searchPK
     */
    public String getSearchPK() {
        return searchPK;
    }

    /**
     * @param searchPK the searchPK to set
     */
    public void setSearchPK(String searchPK) {
        this.searchPK = searchPK;
    }

}
