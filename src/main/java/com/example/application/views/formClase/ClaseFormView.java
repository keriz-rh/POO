package com.example.application.views.formClase;

import com.example.application.controlador.ClaseController;
import com.example.application.modelo.*;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
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
import java.util.Locale;

@PageTitle("Educantrol - Clases")
@Route(value = "clase-form", layout = MainLayout.class)
public class ClaseFormView extends Composite<VerticalLayout> {
    private final ClaseController controller;

    // Campos de la vista
    private final ComboBox<Periodo> periodoField = new ComboBox<>("Periodo");
    private final ComboBox<Materia> materiaField = new ComboBox<>("Materia");
    private final ComboBox<Profesor2> profesorField = new ComboBox<>("Profesor");
    private final TimePicker horaInicioField = new TimePicker("Hora de Inicio");
    private final TimePicker horaFinField = new TimePicker("Hora de Fin");
    private final ComboBox<String> diaField = new ComboBox<>("Día");
    private final TextField aulaField = new TextField("Aula");

    // Grid principal de clases
    private final Grid<Clase> clasesGrid = new Grid<>(Clase.class, false);

    // Botones para filtrado
    private final Button btnFiltrarPorProfesor = new Button("Filtrar por Profesor");
    private final Button btnFiltrarPorMateria = new Button("Filtrar por Materia");
    private final Button btnFiltrarPorPeriodo = new Button("Filtrar por Periodo");

    public ClaseFormView(ClaseController controller) {
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
        createGrid();
        
        // Construcción del layout
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        
        formLayout2Col.add(periodoField, materiaField, profesorField, 
                          diaField, horaInicioField, horaFinField, aulaField);
        
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary, buttonSecondary, 
                     btnFiltrarPorProfesor, btnFiltrarPorMateria, btnFiltrarPorPeriodo);
        
        getContent().add(layoutColumn2);
        getContent().add(clasesGrid);
        
        // Configuración de eventos
        buttonPrimary.addClickListener(e -> saveClase());
        buttonSecondary.addClickListener(e -> resetFields());
        btnFiltrarPorProfesor.addClickListener(e -> filtrarPorProfesor());
        btnFiltrarPorMateria.addClickListener(e -> filtrarPorMateria());
        btnFiltrarPorPeriodo.addClickListener(e -> filtrarPorPeriodo());
    }

    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        
        layout.setWidth("100%");
        layout.setMaxWidth("800px");
        layout.setAlignItems(Alignment.STRETCH);
        
        title.setText("Información de la Clase");
        title.setWidth("100%");
        
        form.setWidth("100%");
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );
        
        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
        buttons.setJustifyContentMode(JustifyContentMode.START);
    }

    private void configureFields() {
        // Configurar ComboBoxes
        periodoField.setItems(controller.getAllPeriodos());
        periodoField.setItemLabelGenerator(Periodo::getNombre);
        periodoField.setRequired(true);
        
        materiaField.setItems(controller.getAllMaterias());
        materiaField.setItemLabelGenerator(Materia::getNombre);
        materiaField.setRequired(true);
        
        profesorField.setItems(controller.getAllProfesores());
        profesorField.setItemLabelGenerator(p -> p.getNombre() + " " + p.getApellido());
        profesorField.setRequired(true);

           // Configurar TimePickers
           horaInicioField.setStep(Duration.ofMinutes(15));
           horaFinField.setStep(Duration.ofMinutes(15));
           horaInicioField.setRequired(true);
           horaFinField.setRequired(true);

        // Configurar ComboBox de días
        diaField.setItems("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado");
        diaField.setRequired(true);

        // Configurar campo de aula
        aulaField.setRequired(true);
        aulaField.setMaxLength(50);
    }

    private void configureButtons(Button save, Button cancel) {
        save.setText("Guardar");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        cancel.setText("Cancelar");
        cancel.setWidth("min-content");
        
        btnFiltrarPorProfesor.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnFiltrarPorMateria.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        btnFiltrarPorPeriodo.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
    }

    private void createGrid() {
        clasesGrid.addColumn(Clase::getIdClase).setHeader("ID").setAutoWidth(true);
        clasesGrid.addColumn(clase -> clase.getPeriodo().getNombre())
                 .setHeader("Periodo").setSortable(true);
        clasesGrid.addColumn(clase -> clase.getMateria().getNombre())
                 .setHeader("Materia").setSortable(true);
        clasesGrid.addColumn(clase -> clase.getProfesor().getNombre() + " " + clase.getProfesor().getApellido())
                 .setHeader("Profesor").setSortable(true);
        clasesGrid.addColumn(clase -> clase.getHorario().getDia())
                 .setHeader("Día").setSortable(true);
        clasesGrid.addColumn(clase -> clase.getHorario().getHoraInicio().toString())
                 .setHeader("Hora Inicio").setSortable(true);
        clasesGrid.addColumn(clase -> clase.getHorario().getHoraFin().toString())
                 .setHeader("Hora Fin").setSortable(true);
        clasesGrid.addColumn(clase -> clase.getHorario().getAula())
                 .setHeader("Aula").setSortable(true);
        
        clasesGrid.addColumn(new ComponentRenderer<>(clase -> {
            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(clase));

            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteClase(clase));

            return new HorizontalLayout(editButton, deleteButton);
        })).setHeader("Acciones");

        clasesGrid.setWidth("100%");
        refreshGrid();
    }

    private void openEditDialog(Clase clase) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        // Campos del formulario de edición
        ComboBox<Periodo> periodoEdit = new ComboBox<>("Periodo");
        ComboBox<Materia> materiaEdit = new ComboBox<>("Materia");
        ComboBox<Profesor2> profesorEdit = new ComboBox<>("Profesor");
        TimePicker horaInicioEdit = new TimePicker("Hora de Inicio");
        TimePicker horaFinEdit = new TimePicker("Hora de Fin");
        ComboBox<String> diaEdit = new ComboBox<>("Día");
        TextField aulaEdit = new TextField("Aula");

        // Configurar campos
        periodoEdit.setItems(controller.getAllPeriodos());
        periodoEdit.setItemLabelGenerator(Periodo::getNombre);
        
        materiaEdit.setItems(controller.getAllMaterias());
        materiaEdit.setItemLabelGenerator(Materia::getNombre);
        
        profesorEdit.setItems(controller.getAllProfesores());
        profesorEdit.setItemLabelGenerator(p -> p.getNombre() + " " + p.getApellido());
        
        diaEdit.setItems("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado");

        // Configurar TimePickers
        horaInicioEdit.setStep(Duration.ofMinutes(5));
        horaFinEdit.setStep(Duration.ofMinutes(5));

        // Establecer valores actuales
        periodoEdit.setValue(clase.getPeriodo());
        materiaEdit.setValue(clase.getMateria());
        profesorEdit.setValue(clase.getProfesor());
        
        if (clase.getHorario() != null) {
            horaInicioEdit.setValue(clase.getHorario().getHoraInicio());
            horaFinEdit.setValue(clase.getHorario().getHoraFin());
            diaEdit.setValue(clase.getHorario().getDia());
            aulaEdit.setValue(clase.getHorario().getAula());
        }

        formLayout.add(periodoEdit, materiaEdit, profesorEdit, 
                      diaEdit, horaInicioEdit, horaFinEdit, aulaEdit);

        Button saveButton = new Button("Guardar", event -> {
            try {
                // Crear un nuevo objeto Horario
                Horario horario = new Horario();
                horario.setHoraInicio(horaInicioEdit.getValue());
                horario.setHoraFin(horaFinEdit.getValue());
                horario.setDia(diaEdit.getValue());
                horario.setAula(aulaEdit.getValue());
                
                // Si el horario actual tiene ID, establecerlo en el nuevo horario
                if (clase.getHorario() != null && clase.getHorario().getId() != null) {
                    horario.setId(clase.getHorario().getId());
                }

                // Actualizar la clase
                clase.setPeriodo(periodoEdit.getValue());
                clase.setMateria(materiaEdit.getValue());
                clase.setProfesor(profesorEdit.getValue());
                clase.setHorario(horario);

                controller.save(clase);
                refreshGrid();
                dialog.close();
                Notification.show("Clase actualizada correctamente", 
                                3000, Notification.Position.TOP_CENTER);
            } catch (Exception e) {
                Notification.show("Error al actualizar la clase: " + e.getMessage(), 
                                3000, Notification.Position.TOP_CENTER);
            }
        });

        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.add(new H3("Editar Clase"));
        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void deleteClase(Clase clase) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new H3("¿Estás seguro de que deseas eliminar esta clase?"));

        Button confirmButton = new Button("Eliminar", event -> {
            try {
                controller.delete(clase);
                refreshGrid();
                Notification.show("Clase eliminada correctamente", 
                                3000, Notification.Position.TOP_CENTER);
                confirmDialog.close();
            } catch (Exception e) {
                Notification.show("Error al eliminar la clase: " + e.getMessage(), 
                                3000, Notification.Position.TOP_CENTER);
            }
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }

    private void saveClase() {
        try {
            if (!validateFields()) {
                return;
            }

            Clase clase = new Clase();
            clase.setPeriodo(periodoField.getValue());
            clase.setMateria(materiaField.getValue());
            clase.setProfesor(profesorField.getValue());
            
            Horario horario = new Horario();
            horario.setHoraInicio(horaInicioField.getValue());
            horario.setHoraFin(horaFinField.getValue());
            horario.setDia(diaField.getValue());
            horario.setAula(aulaField.getValue());
            
            clase.setHorario(horario);

            controller.save(clase);
            Notification.show("Clase guardada correctamente", 
                            3000, Notification.Position.TOP_CENTER);
            resetFields();
            refreshGrid();
        } catch (Exception e) {
            Notification.show("Error al guardar la clase: " + e.getMessage(), 
                            3000, Notification.Position.TOP_CENTER);
        }
    }

    private boolean validateFields() {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("Por favor complete los siguientes campos:\n");

        if (periodoField.getValue() == null) {
            errorMessage.append("- Periodo\n");
            isValid = false;
        }
        if (materiaField.getValue() == null) {
            errorMessage.append("- Materia\n");
            isValid = false;
        }
        if (profesorField.getValue() == null) {
            errorMessage.append("- Profesor\n");
            isValid = false;
        }
        if (horaInicioField.getValue() == null) {
            errorMessage.append("- Hora de Inicio\n");
            isValid = false;
        }
        if (horaFinField.getValue() == null) {
            errorMessage.append("- Hora de Fin\n");
            isValid = false;
        }
        if (diaField.getValue() == null || diaField.getValue().trim().isEmpty()) {
            errorMessage.append("- Día\n");
            isValid = false;
        }
        if (aulaField.getValue() == null || aulaField.getValue().trim().isEmpty()) {
            errorMessage.append("- Aula\n");
            isValid = false;
        }

        if (!isValid) {
            Notification.show(errorMessage.toString(), 
                            5000, Notification.Position.TOP_CENTER);
        }

        return isValid;
    }

    private void resetFields() {
        periodoField.clear();
        materiaField.clear();
        profesorField.clear();
        horaInicioField.clear();
        horaFinField.clear();
        diaField.clear();
        aulaField.clear();
    }

    private void refreshGrid() {
        clasesGrid.setItems(controller.findAll());
    }

    private void filtrarPorProfesor() {
        Profesor2 profesor = profesorField.getValue();
        if (profesor != null) {
            clasesGrid.setItems(controller.findByProfesor(profesor));
        } else {
            Notification.show("Por favor seleccione un profesor para filtrar", 
                            3000, Notification.Position.TOP_CENTER);
        }
    }

    private void filtrarPorMateria() {
        Materia materia = materiaField.getValue();
        if (materia != null) {
            clasesGrid.setItems(controller.findByMateria(materia));
        } else {
            Notification.show("Por favor seleccione una materia para filtrar", 
                            3000, Notification.Position.TOP_CENTER);
        }
    }

    private void filtrarPorPeriodo() {
        Periodo periodo = periodoField.getValue();
        if (periodo != null) {
            clasesGrid.setItems(controller.findByPeriodo(periodo));
        } else {
            Notification.show("Por favor seleccione un periodo para filtrar", 
                            3000, Notification.Position.TOP_CENTER);
        }
    }

    
}