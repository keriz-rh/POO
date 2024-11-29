package com.example.application.views.formExpedienteAcademico;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.application.controlador.ExpedienteAcademicoController;
import com.example.application.modelo.ExpedienteAcademico;
import com.example.application.modelo.Estudiante2;
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

@PageTitle("Educantrol - Expedientes Académicos")
@Route(value = "expediente-academico", layout = MainLayout.class)
public class ExpedienteAcademicoView extends Composite<VerticalLayout> {

    private final ExpedienteAcademicoController controller;

    // Campos de Expediente
    private final NumberField promedioField = new NumberField("Promedio General");
    private final TextField observacionesField = new TextField("Observaciones");
    private final TextField accionesDisciplinariasField = new TextField("Acciones Disciplinarias");
    private final TextField carnetField = new TextField("Carnet del Estudiante");

    // Grid principal de expedientes académicos
    private final Grid<ExpedienteAcademico> expedienteGrid = new Grid<>(ExpedienteAcademico.class, false);

    // Botones para métodos personalizados
    private final Button btnFiltrarPorPromedio = new Button("Filtrar por Promedio");

    @Autowired
    public ExpedienteAcademicoView(ExpedienteAcademicoController controller) {
        this.controller = controller;

        VerticalLayout layoutColumn = new VerticalLayout();
        H3 title = new H3();
        FormLayout formLayout = new FormLayout();
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        Button saveButton = new Button();
        Button cancelButton = new Button();
        
        configureLayout(layoutColumn, title, formLayout, buttonLayout);
        configureFields();
        configureButtons(saveButton, cancelButton);

        layoutColumn.add(title);
        layoutColumn.add(formLayout);
        
        formLayout.add(promedioField, observacionesField, accionesDisciplinariasField, carnetField);
        
        layoutColumn.add(buttonLayout);
        buttonLayout.add(saveButton, cancelButton, btnFiltrarPorPromedio);
        
        getContent().add(layoutColumn);

        createGrid();

        saveButton.addClickListener(e -> saveExpediente());
        btnFiltrarPorPromedio.addClickListener(e -> filtrarPorPromedio());
    }

    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        layout.setWidth("100%");
        layout.setMaxWidth("800px");

        title.setText("Información del Expediente Académico");
        title.setWidth("100%");

        form.setWidth("100%");

        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
    }

    private void configureFields() {
        promedioField.setLabel("Promedio General");
        observacionesField.setLabel("Observaciones");
        accionesDisciplinariasField.setLabel("Acciones Disciplinarias");
        carnetField.setLabel("Carnet del Estudiante");
        carnetField.setPlaceholder("Ingrese el carnet del estudiante");
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
        expedienteGrid.addColumn(ExpedienteAcademico::getId).setHeader("ID").setSortable(true);
        expedienteGrid.addColumn(ExpedienteAcademico::getPromedioGeneral).setHeader("Promedio General").setSortable(true);
        expedienteGrid.addColumn(ExpedienteAcademico::getObservaciones).setHeader("Observaciones").setSortable(true);
        expedienteGrid.addColumn(ExpedienteAcademico::getAccionesDisciplinarias).setHeader("Acciones Disciplinarias").setSortable(true);
        expedienteGrid.addColumn(exp -> exp.getEstudiante() != null ? exp.getEstudiante().getId() : "No Asignado")
                .setHeader("Estudiante ID").setSortable(true);

        expedienteGrid.addColumn(new ComponentRenderer<>(expediente -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(expediente));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteExpediente(expediente));

            return new HorizontalLayout(editButton, deleteButton);
        })).setHeader("Acciones");

        getContent().add(expedienteGrid);
        refreshGrid();
    }

    private void saveExpediente() {
        ExpedienteAcademico expediente = new ExpedienteAcademico();
        expediente.setPromedioGeneral(promedioField.getValue());
        expediente.setObservaciones(observacionesField.getValue());
        expediente.setAccionesDisciplinarias(accionesDisciplinariasField.getValue());
    
        try {
            String carnet = carnetField.getValue();
            Optional<ExpedienteAcademico> expedienteOpt = controller.findByEstudianteCarnet(carnet);
            if (expedienteOpt.isPresent()) {
                expediente.setEstudiante(expedienteOpt.get().getEstudiante());
            } else {
                Notification.show("Carnet de estudiante no válido");
                return;
            }
        } catch (Exception e) {
            Notification.show("Error al asociar el estudiante con el carnet proporcionado");
            return;
        }
        
        controller.save(expediente);
        Notification.show("Expediente guardado correctamente");
        resetFields();
        refreshGrid();
    }

    private void resetFields() {
        promedioField.clear();
        observacionesField.clear();
        accionesDisciplinariasField.clear();
        carnetField.clear();
    }

    private void refreshGrid() {
        expedienteGrid.setItems(controller.findAll());
    }

    private void openEditDialog(ExpedienteAcademico expediente) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        NumberField promedioEdit = new NumberField("Promedio General");
        TextField observacionesEdit = new TextField("Observaciones");
        TextField accionesEdit = new TextField("Acciones Disciplinarias");

        promedioEdit.setValue(expediente.getPromedioGeneral());
        observacionesEdit.setValue(expediente.getObservaciones());
        accionesEdit.setValue(expediente.getAccionesDisciplinarias());

        formLayout.add(promedioEdit, observacionesEdit, accionesEdit);

        Button saveButton = new Button("Guardar", event -> {
            expediente.setPromedioGeneral(promedioEdit.getValue());
            expediente.setObservaciones(observacionesEdit.getValue());
            expediente.setAccionesDisciplinarias(accionesEdit.getValue());

            controller.save(expediente);
            refreshGrid();
            dialog.close();
            Notification.show("Expediente actualizado");
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void deleteExpediente(ExpedienteAcademico expediente) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar este expediente?"));

        Button confirmButton = new Button("Eliminar", event -> {
            controller.delete(expediente);
            refreshGrid();
            Notification.show("Expediente eliminado");
            confirmDialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());

        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }

    private void filtrarPorPromedio() {
        Double minPromedio = promedioField.getValue();
        expedienteGrid.setItems(controller.findByPromedioGeneralGreaterThan(minPromedio));
    }

    private void filtrarPorCarnet() {
        String carnet = carnetField.getValue();
        Optional<ExpedienteAcademico> expedienteOpt = controller.findByEstudianteCarnet(carnet);
    
        // Convierte el Optional en una lista y usa setItems
        expedienteGrid.setItems(expedienteOpt.map(List::of).orElseGet(List::of));
    }
}