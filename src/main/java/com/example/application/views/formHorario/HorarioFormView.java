package com.example.application.views.formHorario;

import java.util.List; 
import com.example.application.controlador.HorarioController;
import com.example.application.controlador.MateriaController;
import com.example.application.controlador.PeriodoController;
import com.example.application.controlador.ProfesorController;
import com.example.application.modelo.Horario;
import com.example.application.modelo.Materia;
import com.example.application.modelo.Periodo;
import com.example.application.modelo.Profesor2;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
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
import java.time.LocalTime;


@PageTitle("Educantrol - Horarios")
@Route(value = "horario", layout = MainLayout.class)
public class HorarioFormView extends Composite<VerticalLayout> {

    private final HorarioController controller;
    private final MateriaController materiaController;
    private final ProfesorController profesorController;
    private final PeriodoController periodoController;

    // Campos del formulario
    private final ComboBox<String> diaField = new ComboBox<>("Día");
    private final TimePicker horaInicioField = new TimePicker("Hora de inicio");
    private final TimePicker horaFinField = new TimePicker("Hora de fin");
    private final TextField aulaField = new TextField("Aula");
    private final ComboBox<Materia> materiaField = new ComboBox<>("Materia");
    private final ComboBox<Profesor2> profesorField = new ComboBox<>("Profesor");
    private final ComboBox<Periodo> periodoField = new ComboBox<>("Periodo");

    // Grid principal de horarios
    private final Grid<Horario> horariosGrid = new Grid<>(Horario.class, false);

    // Botones de filtrado
    private final Button btnFiltrarPorDia = new Button("Filtrar por Día");
    private final Button btnFiltrarPorAula = new Button("Filtrar por Aula");
    private final Button btnFiltrarPorProfesor = new Button("Filtrar por Profesor");
    private final Button btnFiltrarPorMateria = new Button("Filtrar por Materia");
    private final Button btnFiltrarPorPeriodo = new Button("Filtrar por Periodo");
    private final Button btnLimpiarFiltros = new Button("Limpiar Filtros");

    public HorarioFormView(HorarioController controller, 
                          MateriaController materiaController,
                          ProfesorController profesorController,
                          PeriodoController periodoController) {
        this.controller = controller;
        this.materiaController = materiaController;
        this.profesorController = profesorController;
        this.periodoController = periodoController;

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

        formLayout2Col.add(periodoField);
        formLayout2Col.add(materiaField);
        formLayout2Col.add(profesorField);
        formLayout2Col.add(diaField);
        formLayout2Col.add(horaInicioField);
        formLayout2Col.add(horaFinField);
        formLayout2Col.add(aulaField);

        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary);
        layoutRow.add(buttonSecondary);

        // Layout para botones de filtro
        HorizontalLayout filterButtons = new HorizontalLayout();
        filterButtons.setSpacing(true);
        filterButtons.add(btnFiltrarPorDia, btnFiltrarPorAula, btnFiltrarPorProfesor,
                         btnFiltrarPorMateria, btnFiltrarPorPeriodo, btnLimpiarFiltros);
        layoutColumn2.add(filterButtons);

        getContent().add(layoutColumn2);

        createGrid();

        // Configurar listeners
        buttonPrimary.addClickListener(e -> saveHorario());
        btnFiltrarPorDia.addClickListener(e -> filtrarPorDia());
        btnFiltrarPorAula.addClickListener(e -> filtrarPorAula());
        btnFiltrarPorProfesor.addClickListener(e -> filtrarPorProfesor());
        btnFiltrarPorMateria.addClickListener(e -> filtrarPorMateria());
        btnFiltrarPorPeriodo.addClickListener(e -> filtrarPorPeriodo());
        btnLimpiarFiltros.addClickListener(e -> limpiarFiltros());

        // Configurar estilos de botones
        configurarEstilosBotones();
    }

    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        layout.setWidth("100%");
        layout.setMaxWidth("1200px");

        title.setText("Gestión de horarios");
        title.setWidth("100%");

        form.setWidth("100%");
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2),
            new FormLayout.ResponsiveStep("800px", 3)
        );

        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
    }

    private void configureFields() {
        // Configurar ComboBox día
        diaField.setItems("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
        diaField.setRequired(true);
        diaField.setPlaceholder("Seleccione un día");

        // Configurar TimePickers
        horaInicioField.setStep(Duration.ofMinutes(15));
        horaFinField.setStep(Duration.ofMinutes(15));
        horaInicioField.setRequired(true);
        horaFinField.setRequired(true);

        // Campo aula
        aulaField.setRequired(true);
        aulaField.setMaxLength(50);
        aulaField.setPlaceholder("Ingrese el aula");

        //Combobox materia
        List<Materia> materias = materiaController.findAll();
        if (materias != null && !materias.isEmpty()) {
            materiaField.setItems(materias);
        }
        materiaField.setItemLabelGenerator(Materia::getNombre);
        materiaField.setRequired(true);
        materiaField.setPlaceholder("Seleccione una materia");

        //Combobox profesores
        List<Profesor2> profesores = profesorController.findAll();
        if (profesores != null && !profesores.isEmpty()) {
            profesorField.setItems(profesores);
        }
        profesorField.setItemLabelGenerator(p -> p.getNombre() + " " + p.getApellido());
        profesorField.setRequired(true);
        profesorField.setPlaceholder("Seleccione un profesor");

        // Solo mostrar períodos activos en el combo
        List<Periodo> periodosActivos = periodoController.findPeriodosActivos();
        if (periodosActivos != null && !periodosActivos.isEmpty()) {
            periodoField.setItems(periodosActivos);
        }
        periodoField.setItems(periodosActivos);
        periodoField.setItemLabelGenerator(Periodo::getNombre);
        periodoField.setRequired(true);
        periodoField.setPlaceholder("Seleccione un periodo");

       // Validación de horario
       horaFinField.addValueChangeListener(event -> {
        if (horaInicioField.getValue() != null && event.getValue() != null) {
            if (event.getValue().isBefore(horaInicioField.getValue())) {
                Notification.show("La hora de fin debe ser posterior a la hora de inicio")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                horaFinField.clear();
            }
        }
    });
    }



    private void configureButtons(Button save, Button cancel) {
        save.setText("Guardar");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancel.setText("Cancelar");
        cancel.setWidth("min-content");
        cancel.addClickListener(e -> resetFields());
    }

    private void configurarEstilosBotones() {
        btnFiltrarPorDia.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnFiltrarPorAula.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnFiltrarPorProfesor.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnFiltrarPorMateria.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnFiltrarPorPeriodo.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnLimpiarFiltros.addThemeVariants(ButtonVariant.LUMO_ERROR);
    }

    private void createGrid() {
        horariosGrid.addColumn(Horario::getDia).setHeader("Día").setSortable(true);
        horariosGrid.addColumn(Horario::getHoraInicio).setHeader("Hora Inicio").setSortable(true);
        horariosGrid.addColumn(Horario::getHoraFin).setHeader("Hora Fin").setSortable(true);
        horariosGrid.addColumn(Horario::getAula).setHeader("Aula").setSortable(true);
        horariosGrid.addColumn(horario -> horario.getMateria().getNombre())
                   .setHeader("Materia").setSortable(true);
        horariosGrid.addColumn(horario -> 
            horario.getProfesor().getNombre() + " " + horario.getProfesor().getApellido())
                   .setHeader("Profesor").setSortable(true);
        horariosGrid.addColumn(horario -> horario.getPeriodo().getNombre())
                   .setHeader("Periodo").setSortable(true);

        horariosGrid.addColumn(new ComponentRenderer<>(horario -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(horario));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteHorario(horario));

            return new HorizontalLayout(editButton, deleteButton);
        })).setHeader("Acciones");

        getContent().add(horariosGrid);
        refreshGrid();
    }

    private void saveHorario() {
        if (!validateForm()) {
            return;
        }

        Horario horario = new Horario();
        horario.setDia(diaField.getValue());
        horario.setHoraInicio(horaInicioField.getValue());
        horario.setHoraFin(horaFinField.getValue());
        horario.setAula(aulaField.getValue());
        horario.setMateria(materiaField.getValue());
        horario.setProfesor(profesorField.getValue());
        horario.setPeriodo(periodoField.getValue());

        try {
            controller.save(horario);
            Notification.show("Horario guardado correctamente")
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            resetFields();
            refreshGrid();
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage())
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private boolean validateForm() {
        if (diaField.isEmpty() || horaInicioField.isEmpty() || horaFinField.isEmpty() || 
            aulaField.isEmpty() || materiaField.isEmpty() || profesorField.isEmpty() ||
            periodoField.isEmpty()) {
            Notification.show("Por favor, complete todos los campos requeridos")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        if (horaFinField.getValue().isBefore(horaInicioField.getValue())) {
            Notification.show("La hora de fin debe ser posterior a la hora de inicio")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        // Validar horario de operación (7 AM a 10 PM)
        LocalTime inicioOperacion = LocalTime.of(7, 0);
        LocalTime finOperacion = LocalTime.of(22, 0);
        if (horaInicioField.getValue().isBefore(inicioOperacion) || 
            horaFinField.getValue().isAfter(finOperacion)) {
            Notification.show("El horario debe estar entre las 7:00 AM y 10:00 PM")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        return true;
    }

    private void resetFields() {
        diaField.clear();
        horaInicioField.clear();
        horaFinField.clear();
        aulaField.clear();
        materiaField.clear();
        profesorField.clear();
        periodoField.clear();
    }

    private void refreshGrid() {
        List<Horario> horarios = controller.findAll();
        if (horarios != null) {
            horariosGrid.setItems(horarios);
        }
    }

    private void openEditDialog(Horario horario) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        // Campos del formulario
        ComboBox<String> diaEdit = new ComboBox<>("Día");
        TimePicker horaInicioEdit = new TimePicker("Hora de inicio");
        TimePicker horaFinEdit = new TimePicker("Hora de fin");
        TextField aulaEdit = new TextField("Aula");
        ComboBox<Materia> materiaEdit = new ComboBox<>("Materia");
        ComboBox<Profesor2> profesorEdit = new ComboBox<>("Profesor");
        ComboBox<Periodo> periodoEdit = new ComboBox<>("Periodo");

        // Configurar campos
        diaEdit.setItems("Lunes", "Martes", "Miércoles", "Jueves", "Viernes");
        horaInicioEdit.setStep(Duration.ofMinutes(15));
        horaFinEdit.setStep(Duration.ofMinutes(15));
        materiaEdit.setItems(materiaController.findAll());
        materiaEdit.setItemLabelGenerator(Materia::getNombre);
        profesorEdit.setItems(profesorController.findAll());
        profesorEdit.setItemLabelGenerator(p -> p.getNombre() + " " + p.getApellido());
        periodoEdit.setItems(periodoController.findPeriodosActivos());
        periodoEdit.setItemLabelGenerator(Periodo::getNombre);

        // Establecer valores actuales
        diaEdit.setValue(horario.getDia());
        horaInicioEdit.setValue(horario.getHoraInicio());
        horaFinEdit.setValue(horario.getHoraFin());
        aulaEdit.setValue(horario.getAula());
        materiaEdit.setValue(horario.getMateria());
        profesorEdit.setValue(horario.getProfesor());
        periodoEdit.setValue(horario.getPeriodo());

        formLayout.add(periodoEdit, materiaEdit, profesorEdit, 
                      diaEdit, horaInicioEdit, horaFinEdit, aulaEdit);


        Button saveButton = new Button("Guardar", event -> {
            if (validateEditForm(diaEdit, horaInicioEdit, horaFinEdit, aulaEdit,
                               materiaEdit, profesorEdit, periodoEdit)) {
                horario.setDia(diaEdit.getValue());
                horario.setHoraInicio(horaInicioEdit.getValue());
                horario.setHoraFin(horaFinEdit.getValue());
                horario.setAula(aulaEdit.getValue());
                horario.setMateria(materiaEdit.getValue());
                horario.setProfesor(profesorEdit.getValue());
                horario.setPeriodo(periodoEdit.getValue());

                try {
                    controller.save(horario);
                    refreshGrid();
                    dialog.close();
                    Notification.show("Horario actualizado correctamente")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } catch (Exception e) {
                    Notification.show("Error: " + e.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());

        dialog.add(new H3("Editar Horario"));
        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }


    private boolean validateEditForm(ComboBox<String> diaEdit, TimePicker horaInicioEdit,
                                   TimePicker horaFinEdit, TextField aulaEdit,
                                   ComboBox<Materia> materiaEdit, ComboBox<Profesor2> profesorEdit,
                                   ComboBox<Periodo> periodoEdit) {
        if (diaEdit.isEmpty() || horaInicioEdit.isEmpty() || horaFinEdit.isEmpty() ||
            aulaEdit.isEmpty() || materiaEdit.isEmpty() || profesorEdit.isEmpty() ||
            periodoEdit.isEmpty()) {
            Notification.show("Por favor, complete todos los campos requeridos")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        if (horaFinEdit.getValue().isBefore(horaInicioEdit.getValue())) {
            Notification.show("La hora de fin debe ser posterior a la hora de inicio")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        LocalTime inicioOperacion = LocalTime.of(7, 0);
        LocalTime finOperacion = LocalTime.of(22, 0);
        if (horaInicioEdit.getValue().isBefore(inicioOperacion) || 
            horaFinEdit.getValue().isAfter(finOperacion)) {
            Notification.show("El horario debe estar entre las 7:00 AM y 10:00 PM")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }

        return true;
    }

    private void deleteHorario(Horario horario) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar este horario?"));


        Button confirmButton = new Button("Eliminar", event -> {
            try {
                controller.delete(horario);
                refreshGrid();
                Notification.show("Horario eliminado correctamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                confirmDialog.close();
            } catch (Exception e) {
                Notification.show("Error al eliminar el horario: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());

        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }

    private void filtrarPorDia() {
        if (diaField.isEmpty()) {
            Notification.show("Por favor, seleccione un día para filtrar")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        horariosGrid.setItems(controller.findByDia(diaField.getValue()));
    }

    private void filtrarPorAula() {
        if (aulaField.isEmpty()) {
            Notification.show("Por favor, ingrese un aula para filtrar")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        horariosGrid.setItems(controller.findByAula(aulaField.getValue()));
    }

    private void filtrarPorProfesor() {
        if (profesorField.isEmpty()) {
            Notification.show("Por favor, seleccione un profesor para filtrar")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        horariosGrid.setItems(controller.findByProfesor(profesorField.getValue()));
    }

    private void filtrarPorMateria() {
        if (materiaField.isEmpty()) {
            Notification.show("Por favor, seleccione una materia para filtrar")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        horariosGrid.setItems(controller.findByMateria(materiaField.getValue()));
    }

    private void filtrarPorPeriodo() {
        if (periodoField.isEmpty()) {
            Notification.show("Por favor, seleccione un periodo para filtrar")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        horariosGrid.setItems(controller.findByPeriodo(periodoField.getValue()));
    }

    private void limpiarFiltros() {
        resetFields();
        refreshGrid();
        Notification.show("Filtros eliminados")
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

}