package com.example.application.views.formMateria;

import com.example.application.controlador.MateriaController;
import com.example.application.modelo.Materia;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Educantrol - Materias")
@Route(value = "materia-form", layout = MainLayout.class)
public class MateriaFormView extends Composite<VerticalLayout> {
    
    private final MateriaController controller;
    
    // Campos del formulario
    private final TextField nombreField = new TextField("Nombre");
    private final TextField codigoField = new TextField("Código");
    private final TextArea descripcionField = new TextArea("Descripción");
    
    // Grid principal de materias
    private final Grid<Materia> materiasGrid = new Grid<>(Materia.class, false);
    
    // Botones para filtrar
    private final Button btnFiltrarPorNombre = new Button("Filtrar por Nombre");
    private final Button btnFiltrarPorCodigo = new Button("Filtrar por Código");
    private final Button btnLimpiarFiltros = new Button("Limpiar Filtros");
    
    public MateriaFormView(MateriaController controller) {
        this.controller = controller;
        
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        
        configureLayout(layoutColumn2, h3, formLayout2Col, layoutRow);
        configureFields();
        configureButtons(buttonPrimary, buttonSecondary);
        
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        
        formLayout2Col.add(nombreField);
        formLayout2Col.add(codigoField);
        formLayout2Col.add(descripcionField);
        
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
        
        // Crear layout para botones de filtro
        HorizontalLayout filterButtons = new HorizontalLayout();
        filterButtons.setSpacing(true);
        filterButtons.add(btnFiltrarPorNombre, btnFiltrarPorCodigo, btnLimpiarFiltros);
        layoutColumn2.add(filterButtons);
        
        getContent().add(layoutColumn2);
        
        createGrid();
        
        // Configurar listeners de botones
        buttonPrimary.addClickListener(e -> saveMateria());
        btnFiltrarPorNombre.addClickListener(e -> filtrarPorNombre());
        btnFiltrarPorCodigo.addClickListener(e -> filtrarPorCodigo());
        btnLimpiarFiltros.addClickListener(e -> limpiarFiltros());
        
        // Estilo para los botones de filtro
        btnFiltrarPorNombre.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnFiltrarPorCodigo.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }
    
    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        
        layout.setWidth("100%");
        layout.setMaxWidth("800px");
        
        title.setText("Gestión de Materias");
        title.setWidth("100%");
        
        form.setWidth("100%");
        
        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
    }
    
    private void configureFields() {
        nombreField.setRequired(true);
        nombreField.setMinLength(2);
        nombreField.setMaxLength(100);
        nombreField.setPlaceholder("Ingrese el nombre de la materia");
        
        codigoField.setRequired(true);
        codigoField.setMaxLength(20);
        codigoField.setPlaceholder("Ingrese el código de la materia");
        
        descripcionField.setMaxLength(500);
        descripcionField.setMinHeight("100px");
        descripcionField.setPlaceholder("Ingrese la descripción de la materia");
    }
    
    private void configureButtons(Button save, Button cancel) {
        save.setText("Guardar");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        cancel.setText("Cancelar");
        cancel.setWidth("min-content");
        cancel.addClickListener(e -> resetFields());
    }
    
    private void createGrid() {
        materiasGrid.addColumn(Materia::getIdMateria).setHeader("ID").setSortable(true);
        materiasGrid.addColumn(Materia::getCodigo).setHeader("Código").setSortable(true);
        materiasGrid.addColumn(Materia::getNombre).setHeader("Nombre").setSortable(true);
        materiasGrid.addColumn(Materia::getDescripcion).setHeader("Descripción").setSortable(true);
        
        materiasGrid.addColumn(new ComponentRenderer<>(materia -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(materia));
            
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteMateria(materia));
            
            return new HorizontalLayout(editButton, deleteButton);
        })).setHeader("Acciones");
        
        getContent().add(materiasGrid);
        refreshGrid();
    }
    
    private void saveMateria() {
        if (validateFields()) {
            Materia materia = new Materia();
            materia.setNombre(nombreField.getValue());
            materia.setCodigo(codigoField.getValue());
            materia.setDescripcion(descripcionField.getValue());
            
            try {
                controller.save(materia);
                Notification.show("Materia guardada correctamente");
                resetFields();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Error al guardar la materia: El código ya existe");
            }
        }
    }
    
    private boolean validateFields() {
        if (nombreField.isEmpty()) {
            Notification.show("El nombre es requerido");
            return false;
        }
        if (codigoField.isEmpty()) {
            Notification.show("El código es requerido");
            return false;
        }
        return true;
    }
    
    private void resetFields() {
        nombreField.clear();
        codigoField.clear();
        descripcionField.clear();
    }
    
    private void refreshGrid() {
        materiasGrid.setItems(controller.findAll());
    }
    
    private void openEditDialog(Materia materia) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        
        TextField nombreEdit = new TextField("Nombre");
        TextField codigoEdit = new TextField("Código");
        TextArea descripcionEdit = new TextArea("Descripción");
        
        nombreEdit.setValue(materia.getNombre());
        codigoEdit.setValue(materia.getCodigo());
        descripcionEdit.setValue(materia.getDescripcion());
        
        formLayout.add(nombreEdit, codigoEdit, descripcionEdit);
        
        Button saveButton = new Button("Guardar", event -> {
            if (!nombreEdit.isEmpty() && !codigoEdit.isEmpty()) {
                materia.setNombre(nombreEdit.getValue());
                materia.setCodigo(codigoEdit.getValue());
                materia.setDescripcion(descripcionEdit.getValue());
                
                try {
                    controller.save(materia);
                    refreshGrid();
                    dialog.close();
                    Notification.show("Materia actualizada");
                } catch (Exception e) {
                    Notification.show("Error al actualizar: El código ya existe");
                }
            }
        });
        
        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        
        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }
    
    private void deleteMateria(Materia materia) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar esta materia?"));
        
        Button confirmButton = new Button("Eliminar", event -> {
            try {
                controller.delete(materia);
                refreshGrid();
                Notification.show("Materia eliminada");
                confirmDialog.close();
            } catch (Exception e) {
                Notification.show("Error al eliminar la materia: La materia está siendo utilizada");
            }
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());
        
        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }
    
    private void filtrarPorNombre() {
        String nombre = nombreField.getValue();
        if (!nombre.isEmpty()) {
            materiasGrid.setItems(controller.findByNombreContaining(nombre));
        } else {
            Notification.show("Ingrese un nombre para filtrar");
        }
    }
    
    private void filtrarPorCodigo() {
        String codigo = codigoField.getValue();
        if (!codigo.isEmpty()) {
            controller.findByCodigo(codigo).ifPresentOrElse(
                materia -> materiasGrid.setItems(java.util.Collections.singletonList(materia)),
                () -> {
                    Notification.show("No se encontró ninguna materia con ese código");
                    refreshGrid();
                }
            );
        } else {
            Notification.show("Ingrese un código para filtrar");
        }
    }
    
    private void limpiarFiltros() {
        resetFields();
        refreshGrid();
        Notification.show("Filtros eliminados");
    }
}