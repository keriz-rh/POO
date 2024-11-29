package com.example.application.views.formProfesor;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.application.controlador.ProfesorController;
import com.example.application.modelo.Clase;
import com.example.application.modelo.Profesor2;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.component.grid.Grid;

@PageTitle("Educantrol - Profesores")
@Route(value = "profesor-form", layout = MainLayout.class)
public class ProfesorFormView extends Composite<VerticalLayout> {
    
    private final ProfesorController controller;
    
    // Campos heredados de Persona
    private final TextField nombreField = new TextField("Nombre");
    private final TextField apellidoField = new TextField("Apellido");
    private final TextField telefonoField = new TextField("Teléfono");
    private final TextField direccionField = new TextField("Dirección");
    private final NumberField edadField = new NumberField("Edad");

    // Campos específicos de Profesor2
    private final TextField especialidadField = new TextField("Especialidad");

    // Grid principal de profesores
    private final Grid<Profesor2> profesoresGrid = new Grid<>(Profesor2.class, false);

    // Nuevos botones para métodos personalizados
    private final Button btnFiltrarPorEspecialidad = new Button("Filtrar por Especialidad");
    private final Button btnFiltrarPorDisponibilidad = new Button("Filtrar por Disponibilidad");
    private final Button btnFiltrarPorClases = new Button("Filtrar por Número de Clases");

    public ProfesorFormView(ProfesorController controller) {
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
        formLayout2Col.add(apellidoField);
        formLayout2Col.add(edadField);
        formLayout2Col.add(telefonoField);
        formLayout2Col.add(direccionField);
        formLayout2Col.add(especialidadField);
        
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
        layoutRow.add(btnFiltrarPorEspecialidad);
        layoutRow.add(btnFiltrarPorDisponibilidad);
        layoutRow.add(btnFiltrarPorClases);
        
        getContent().add(layoutColumn2);
        
        createGrid();
        
        buttonPrimary.addClickListener(e -> saveProfesor());
        btnFiltrarPorEspecialidad.addClickListener(e -> filtrarPorEspecialidad());
        btnFiltrarPorDisponibilidad.addClickListener(e -> filtrarPorDisponibilidad());
        btnFiltrarPorClases.addClickListener(e -> filtrarPorClases());
    }

    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        
        layout.setWidth("100%");
        layout.setMaxWidth("800px");
        
        title.setText("Información del Profesor");
        title.setWidth("100%");
        
        form.setWidth("100%");
        
        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
    }

    private void configureFields() {
        nombreField.setLabel("Nombres");
        apellidoField.setLabel("Apellidos");
        edadField.setLabel("Edad");
        telefonoField.setLabel("Teléfono");
        direccionField.setLabel("Dirección");
        especialidadField.setLabel("Especialidad");
    }

    private void configureButtons(Button save, Button cancel) {
        save.setText("Guardar");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        cancel.setText("Cancelar");
        cancel.setWidth("min-content");
    }

    private void createGrid() {
        profesoresGrid.addColumn(Profesor2::getId).setHeader("CÓDIGO").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getNombre).setHeader("Nombre").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getApellido).setHeader("Apellido").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getEdad).setHeader("Edad").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getTelefono).setHeader("Teléfono").setSortable(true);
        profesoresGrid.addColumn(Profesor2::getEspecialidad).setHeader("Especialidad").setSortable(true);
        
        profesoresGrid.addColumn(new ComponentRenderer<>(profesor -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(profesor));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteProfesor(profesor));

            return new HorizontalLayout(editButton, deleteButton);
        })).setHeader("Acciones");
        
        getContent().add(profesoresGrid);
        refreshGrid();
    }

    private void saveProfesor() {
        Profesor2 profesor = new Profesor2();
        profesor.setNombre(nombreField.getValue());
        profesor.setApellido(apellidoField.getValue());
        profesor.setEdad(edadField.getValue());
        profesor.setDireccion(direccionField.getValue());
        profesor.setTelefono(telefonoField.getValue());
        profesor.setEspecialidad(especialidadField.getValue());
        
        controller.save(profesor);
        
        Notification.show("Profesor guardado correctamente");
        resetFields();
        refreshGrid();
    }

    private void resetFields() {
        nombreField.clear();
        apellidoField.clear();
        edadField.clear();
        telefonoField.clear();
        direccionField.clear();
        especialidadField.clear();
    }

    private void refreshGrid() {
        profesoresGrid.setItems(controller.findAll());
    }

    private void openEditDialog(Profesor2 profesor) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        // Campos del formulario
        TextField nombreEdit = new TextField("Nombre");
        TextField apellidoEdit = new TextField("Apellido");
        TextField telefonoEdit = new TextField("Teléfono");
        TextField direccionEdit = new TextField("Dirección");
        NumberField edadEdit = new NumberField("Edad");
        TextField especialidadEdit = new TextField("Especialidad");

        // Establecer valores actuales
        nombreEdit.setValue(profesor.getNombre());
        apellidoEdit.setValue(profesor.getApellido());
        telefonoEdit.setValue(profesor.getTelefono());
        direccionEdit.setValue(profesor.getDireccion());
        edadEdit.setValue(profesor.getEdad());
        especialidadEdit.setValue(profesor.getEspecialidad());

        formLayout.add(nombreEdit, apellidoEdit, telefonoEdit, direccionEdit, edadEdit, especialidadEdit);

        Button saveButton = new Button("Guardar", event -> {
            profesor.setNombre(nombreEdit.getValue());
            profesor.setApellido(apellidoEdit.getValue());
            profesor.setEdad(edadEdit.getValue());
            profesor.setDireccion(direccionEdit.getValue());
            profesor.setTelefono(telefonoEdit.getValue());
            profesor.setEspecialidad(especialidadEdit.getValue());

            controller.save(profesor);
            refreshGrid();
            dialog.close();
            Notification.show("Profesor actualizado");
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void deleteProfesor(Profesor2 profesor) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar este profesor?"));

        Button confirmButton = new Button("Eliminar", event -> {
            controller.delete(profesor);
            refreshGrid();
            Notification.show("Profesor eliminado");
            confirmDialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());

        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }

    private void filtrarPorEspecialidad() {
        String especialidad = especialidadField.getValue();
        profesoresGrid.setItems(controller.findByEspecialidad(especialidad));
    }

    private void filtrarPorDisponibilidad() {
        int maxClases = 3; // Puedes ajustar este valor según tus necesidades
        profesoresGrid.setItems(controller.findProfesoresDisponibles(maxClases));
    }

    private void filtrarPorClases() {
        int minClases = 5; // Puedes ajustar este valor según tus necesidades
        profesoresGrid.setItems(controller.findProfesoresConMasClases(minClases));
    }
}