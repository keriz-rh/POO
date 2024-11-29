package com.example.application.views.formEvaluacion;

import com.example.application.controlador.EvaluacionController;
import com.example.application.modelo.Clase;
import com.example.application.modelo.Evaluacion;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import java.time.Duration;
import java.time.LocalDate;

@PageTitle("Educantrol - Evaluaciones")
@Route(value = "evaluacion", layout = MainLayout.class)
public class EvaluacionFormView extends Composite<VerticalLayout> {

    private final EvaluacionController controller;

    // Campos del formulario
    private final DatePicker diaField = new DatePicker("Día");
    private final TimePicker horaInicioField = new TimePicker("Hora de inicio");
    private final TimePicker horaFinField = new TimePicker("Hora de fin");
    private final TextField aulaField = new TextField("Aula");
    private final ComboBox<String> tipoField = new ComboBox<>("Tipo");
    private final ComboBox<Clase> claseField = new ComboBox<>("Clase");

    // Grid principal de evaluaciones
    private final Grid<Evaluacion> evaluacionesGrid = new Grid<>(Evaluacion.class, false);

    // Botones de filtrado
    private final Button btnFiltrarPorDia = new Button("Filtrar por Día");
    private final Button btnFiltrarPorAula = new Button("Filtrar por Aula");

    public EvaluacionFormView(EvaluacionController controller) {
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

        formLayout2Col.add(diaField);
        formLayout2Col.add(horaInicioField);
        formLayout2Col.add(horaFinField);
        formLayout2Col.add(aulaField);
        formLayout2Col.add(tipoField);
        formLayout2Col.add(claseField);

        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);
        layoutRow.add(btnFiltrarPorDia);
        layoutRow.add(btnFiltrarPorAula);

        getContent().add(layoutColumn2);

        createGrid();

        buttonPrimary.addClickListener(e -> saveEvaluacion());
        btnFiltrarPorDia.addClickListener(e -> filtrarPorDia());
        btnFiltrarPorAula.addClickListener(e -> filtrarPorAula());
    }

    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        layout.setWidth("100%");
        layout.setMaxWidth("800px");

        title.setText("Información de la Evaluación");
        title.setWidth("100%");

        form.setWidth("100%");

        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
    }

    private void configureFields() {
        // Configurar DatePicker
        diaField.setRequired(true);
        diaField.setMin(LocalDate.now());

        // Configurar TimePickers
        horaInicioField.setStep(Duration.ofMinutes(15));
        horaFinField.setStep(Duration.ofMinutes(15));
        horaInicioField.setRequired(true);
        horaFinField.setRequired(true);

        // Configurar TextField
        aulaField.setRequired(true);
        aulaField.setMaxLength(50);

        // Configurar ComboBox tipo
        tipoField.setItems("Parcial", "Final", "Recuperatorio");
        tipoField.setRequired(true);

        // Configurar ComboBox clase
        claseField.setRequired(true);
        claseField.setItems(controller.getAllClases());  // Cargar las clases
        claseField.setItemLabelGenerator(clase -> 
            String.format("%s - %s - %s", 
                clase.getMateria().getNombre(),
                clase.getProfesor().getNombre(),
                clase.getHorario().getDia()
            )
        );
    }

    private void configureButtons(Button save, Button cancel) {
        save.setText("Guardar");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancel.setText("Cancelar");
        cancel.setWidth("min-content");
    }

    private void createGrid() {
        evaluacionesGrid.addColumn(Evaluacion::getId).setHeader("ID").setSortable(true);
        evaluacionesGrid.addColumn(Evaluacion::getDia).setHeader("Día").setSortable(true);
        evaluacionesGrid.addColumn(Evaluacion::getHoraInicio).setHeader("Hora Inicio").setSortable(true);
        evaluacionesGrid.addColumn(Evaluacion::getHoraFin).setHeader("Hora Fin").setSortable(true);
        evaluacionesGrid.addColumn(Evaluacion::getAula).setHeader("Aula").setSortable(true);
        evaluacionesGrid.addColumn(Evaluacion::getTipo).setHeader("Tipo").setSortable(true);
        evaluacionesGrid.addColumn(evaluacion -> evaluacion.getClase().toString()).setHeader("Clase").setSortable(true);

        evaluacionesGrid.addColumn(new ComponentRenderer<>(evaluacion -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(evaluacion));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteEvaluacion(evaluacion));

            return new HorizontalLayout(editButton, deleteButton);
        })).setHeader("Acciones");

        getContent().add(evaluacionesGrid);
        refreshGrid();
    }

private void saveEvaluacion() {
    try {
        if (!validateForm()) {
            return;
        }

        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setDia(diaField.getValue());
        evaluacion.setHoraInicio(horaInicioField.getValue());
        evaluacion.setHoraFin(horaFinField.getValue());
        evaluacion.setAula(aulaField.getValue());
        evaluacion.setTipo(tipoField.getValue());
        evaluacion.setClase(claseField.getValue());

        try {
            Evaluacion savedEvaluacion = controller.save(evaluacion);
            if (savedEvaluacion != null && savedEvaluacion.getId() != null) {
                Notification.show("Evaluación guardada correctamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                resetFields();
                refreshGrid();
            } else {
                throw new Exception("No se pudo guardar la evaluación");
            }
        } catch (Exception e) {
            Notification.show("Error al guardar la evaluación: " + e.getMessage())
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    } catch (Exception e) {
        Notification.show("Error en el formulario: " + e.getMessage())
            .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}

    private boolean validateForm() {
        if (diaField.isEmpty() || horaInicioField.isEmpty() || horaFinField.isEmpty() || 
            aulaField.isEmpty() || tipoField.isEmpty() || claseField.isEmpty()) {
            Notification.show("Por favor, complete todos los campos requeridos");
            return false;
        }
        if (horaFinField.getValue().isBefore(horaInicioField.getValue())) {
            Notification.show("La hora de fin debe ser posterior a la hora de inicio");
            return false;
        }
        return true;
    }

    private void resetFields() {
        diaField.clear();
        horaInicioField.clear();
        horaFinField.clear();
        aulaField.clear();
        tipoField.clear();
        claseField.clear();
    }

    private void refreshGrid() {
        evaluacionesGrid.setItems(controller.findAll());
    }

    private void openEditDialog(Evaluacion evaluacion) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        // Campos del formulario
        DatePicker diaEdit = new DatePicker("Día");
        TimePicker horaInicioEdit = new TimePicker("Hora de inicio");
        TimePicker horaFinEdit = new TimePicker("Hora de fin");
        TextField aulaEdit = new TextField("Aula");
        ComboBox<String> tipoEdit = new ComboBox<>("Tipo");
        ComboBox<Clase> claseEdit = new ComboBox<>("Clase");

        // Configurar campos
        configureEditFields(diaEdit, horaInicioEdit, horaFinEdit, aulaEdit, tipoEdit, claseEdit);

        // Establecer valores actuales
        diaEdit.setValue(evaluacion.getDia());
        horaInicioEdit.setValue(evaluacion.getHoraInicio());
        horaFinEdit.setValue(evaluacion.getHoraFin());
        aulaEdit.setValue(evaluacion.getAula());
        tipoEdit.setValue(evaluacion.getTipo());
        claseEdit.setValue(evaluacion.getClase());

        formLayout.add(diaEdit, horaInicioEdit, horaFinEdit, aulaEdit, tipoEdit, claseEdit);

        Button saveButton = new Button("Guardar", event -> {
            evaluacion.setDia(diaEdit.getValue());
            evaluacion.setHoraInicio(horaInicioEdit.getValue());
            evaluacion.setHoraFin(horaFinEdit.getValue());
            evaluacion.setAula(aulaEdit.getValue());
            evaluacion.setTipo(tipoEdit.getValue());
            evaluacion.setClase(claseEdit.getValue());

            controller.save(evaluacion);
            refreshGrid();
            dialog.close();
            Notification.show("Evaluación actualizada");
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void configureEditFields(DatePicker diaEdit, TimePicker horaInicioEdit, 
                                   TimePicker horaFinEdit, TextField aulaEdit, 
                                   ComboBox<String> tipoEdit, ComboBox<Clase> claseEdit) {
        diaEdit.setRequired(true);
        horaInicioEdit.setStep(Duration.ofMinutes(15));
        horaFinEdit.setStep(Duration.ofMinutes(15));
        aulaEdit.setRequired(true);
        aulaEdit.setMaxLength(50);
        tipoEdit.setItems("Parcial", "Final", "Recuperatorio");
        tipoEdit.setRequired(true);
        claseEdit.setRequired(true);
        claseEdit.setItems(controller.getAllClases());  // Cargar las clases
        claseEdit.setItemLabelGenerator(clase -> 
            String.format("%s - %s - %s", 
                clase.getMateria().getNombre(),
                clase.getProfesor().getNombre(),
                clase.getHorario().getDia()
            )
        );
    }

    private void deleteEvaluacion(Evaluacion evaluacion) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar esta evaluación?"));

        Button confirmButton = new Button("Eliminar", event -> {
            controller.delete(evaluacion);
            refreshGrid();
            Notification.show("Evaluación eliminada");
            confirmDialog.close();
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());

        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }

    private void filtrarPorDia() {
        if (diaField.isEmpty()) {
            Notification.show("Por favor, seleccione un día para filtrar");
            return;
        }
        evaluacionesGrid.setItems(controller.findByDia(diaField.getValue()));
    }

    private void filtrarPorAula() {
        if (aulaField.isEmpty()) {
            Notification.show("Por favor, ingrese un aula para filtrar");
            return;
        }
        evaluacionesGrid.setItems(controller.findByAula(aulaField.getValue()));
    }
}