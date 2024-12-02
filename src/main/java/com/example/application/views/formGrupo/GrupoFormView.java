package com.example.application.views.formGrupo;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.application.controlador.*;
import com.example.application.modelo.*;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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

@PageTitle("Educantrol - Grupos")
@Route(value = "grupo-form", layout = MainLayout.class)
public class GrupoFormView extends Composite<VerticalLayout> {
    
    private final GrupoController controller;
    private final EstudianteController estudianteController;
    private final HorarioController horarioController;
    
    // Campos del formulario
    private final TextField nombreField = new TextField("Nombre del Grupo");
    private final NumberField capacidadField = new NumberField("Capacidad de Estudiantes");
    
    // Grid principal
    private final Grid<Grupo> gruposGrid = new Grid<>(Grupo.class, false);
    
    public GrupoFormView(
            GrupoController controller,
            EstudianteController estudianteController,
            HorarioController horarioController) {
        
        this.controller = controller;
        this.estudianteController = estudianteController;
        this.horarioController = horarioController;
        
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
        
        formLayout2Col.add(nombreField, capacidadField);
        
        layoutColumn2.add(layoutRow);
        layoutRow.add(buttonPrimary, buttonSecondary);
        
        getContent().add(layoutColumn2);
        
        createGrid();
        
        buttonPrimary.addClickListener(e -> saveGrupo());
        buttonSecondary.addClickListener(e -> resetFields());
    }
    
    private void configureLayout(VerticalLayout layout, H3 title, FormLayout form, HorizontalLayout buttons) {
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        
        layout.setWidth("100%");
        layout.setMaxWidth("800px");
        
        title.setText("Información del Grupo");
        title.setWidth("100%");
        
        form.setWidth("100%");
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );
        
        buttons.addClassName(Gap.MEDIUM);
        buttons.setWidth("100%");
    }
    
    private void configureFields() {
        nombreField.setRequired(true);
        nombreField.setMinLength(2);
        nombreField.setMaxLength(50);
        nombreField.setPlaceholder("Ingrese el nombre del grupo");
        
        capacidadField.setRequired(true);
        capacidadField.setMin(1);
        capacidadField.setValue(1.0);
        capacidadField.setPlaceholder("Ingrese la capacidad del grupo");
    }
    
    private void configureButtons(Button save, Button cancel) {
        save.setText("Guardar");
        save.setWidth("min-content");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        cancel.setText("Cancelar");
        cancel.setWidth("min-content");
    }
    
    private void createGrid() {
        gruposGrid.addColumn(Grupo::getNombre).setHeader("Nombre").setSortable(true);
        gruposGrid.addColumn(Grupo::getCapacidadEstudiantes).setHeader("Capacidad").setSortable(true);
        
        gruposGrid.addColumn(new ComponentRenderer<>(grupo -> {
            Button viewButton = new Button(new Icon(VaadinIcon.EYE));
            viewButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            viewButton.addClickListener(e -> showGrupoDetails(grupo));

            Button editButton = new Button(new Icon(VaadinIcon.EDIT));
            editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            editButton.addClickListener(e -> openEditDialog(grupo));
            
            Button deleteButton = new Button(new Icon(VaadinIcon.TRASH));
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            deleteButton.addClickListener(e -> deleteGrupo(grupo));

            Button addStudentButton = new Button();
            getContent().setWidth("100%");
            getContent().getStyle().set("flex-grow", "1");
            addStudentButton.setText("Agregar Estudiantes");
            addStudentButton.setWidth("min-content");
            addStudentButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            addStudentButton.addClickListener(e -> openAddStudentsDialog(grupo));

            Button addHorarioButton = new Button();
            getContent().setWidth("100%");
            getContent().getStyle().set("flex-grow", "1");
            addHorarioButton.setText("Agregar Horario");
            addHorarioButton.setWidth("min-content");
            addHorarioButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            addHorarioButton.addClickListener(e -> openAddHorariosDialog(grupo));
            
            return new HorizontalLayout(addStudentButton, addHorarioButton, viewButton, editButton, deleteButton);
        })).setHeader("Acciones");
        
        getContent().add(gruposGrid);
        refreshGrid();
    }
    
    private void saveGrupo() {
        if (validateFields()) {
            Grupo grupo = new Grupo();
            grupo.setNombre(nombreField.getValue());
            grupo.setCapacidadEstudiantes(capacidadField.getValue().intValue());
            
            try {
                controller.save(grupo);
                Notification.show("Grupo guardado correctamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                resetFields();
                refreshGrid();
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }
    
    private boolean validateFields() {
        if (nombreField.isEmpty() || capacidadField.isEmpty()) {
            Notification.show("Los campos Nombre y Capacidad son requeridos")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        
        if (capacidadField.getValue() < 1) {
            Notification.show("La capacidad debe ser mayor a 0")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        
        return true;
    }
    
    private void resetFields() {
        nombreField.clear();
        capacidadField.setValue(1.0);
    }
    
    private void refreshGrid() {
        gruposGrid.setItems(controller.findAll());
    }

    private void openAddStudentsDialog(Grupo grupo) {
        Dialog dialog = new Dialog();
        VerticalLayout dialogLayout = new VerticalLayout();
        
        // Grid de estudiantes
        Grid<Estudiante2> estudiantesGrid = new Grid<>(Estudiante2.class, false);
        estudiantesGrid.addColumn(Estudiante2::getNombre).setHeader("Nombre").setSortable(true);
        estudiantesGrid.addColumn(Estudiante2::getApellido).setHeader("Apellido").setSortable(true);
        estudiantesGrid.addColumn(Estudiante2::getCarnet).setHeader("Carnet").setSortable(true);
        
        // Configurar selección múltiple en el grid
        estudiantesGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        
        // Cargar estudiantes
        estudiantesGrid.setItems(estudianteController.findAll());
        
        // Botones
        Button addButton = new Button("Agregar", event -> {
            Set<Estudiante2> selectedStudents = estudiantesGrid.getSelectedItems();
            if (selectedStudents.isEmpty()) {
                Notification.show("Seleccione al menos un estudiante")
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }
            
            try {
                for (Estudiante2 estudiante : selectedStudents) {
                    controller.agregarEstudiante(grupo, estudiante);
                }
                dialog.close();
                refreshGrid();
                Notification.show("Estudiantes agregados correctamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        
        Button cancelButton = new Button("Cancelar", e -> dialog.close());
        
        // Configurar layout
        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        
        dialogLayout.add(
            new H3("Agregar Estudiantes al Grupo"),
            estudiantesGrid,
            buttonLayout
        );
        
        dialog.add(dialogLayout);
        dialog.setWidth("800px");
        dialog.setHeight("600px");
        
        dialog.open();
    }

private void openAddHorariosDialog(Grupo grupo) {
    Dialog dialog = new Dialog();
    VerticalLayout dialogLayout = new VerticalLayout();
    
    // ComboBox de horarios
    ComboBox<Horario> horarioComboBox = new ComboBox<>("Seleccione un Horario");
    horarioComboBox.setWidth("100%");
    
    // Configurar el generador de etiquetas para mostrar la información del horario
    horarioComboBox.setItemLabelGenerator(horario -> 
        horario.getPeriodo().getNombre() + " | " +
        horario.getMateria().getNombre() + " | " +
        "Aula: " + horario.getAula() + " | " +
        horario.getProfesor().getNombre() + " " + horario.getProfesor().getApellido() + " | " +
        horario.getHoraInicio() + " - " + horario.getHoraFin()
    );
    
    // Cargar solo horarios disponibles
    List<Horario> horariosDisponibles = horarioController.findHorariosDisponibles();
    horarioComboBox.setItems(horariosDisponibles);
    
    // Botones
    Button addButton = new Button("Agregar", event -> {
        Horario selectedHorario = horarioComboBox.getValue();
        if (selectedHorario == null) {
            Notification.show("Por favor, seleccione un horario")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }
        
        try {
            controller.agregarHorario(grupo, selectedHorario);
            dialog.close();
            refreshGrid();
            Notification.show("Horario agregado correctamente")
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } catch (Exception e) {
            Notification.show("Error: " + e.getMessage())
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    });
    addButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    
    Button cancelButton = new Button("Cancelar", e -> dialog.close());
    
    // Configurar layout
    HorizontalLayout buttonLayout = new HorizontalLayout(addButton, cancelButton);
    buttonLayout.setJustifyContentMode(JustifyContentMode.END);
    
    dialogLayout.add(
        new H3("Agregar Horario al Grupo"),
        horarioComboBox,
        buttonLayout
    );
    
    dialog.add(dialogLayout);
    dialog.setWidth("600px");
    dialog.setMinHeight("200px");
    
    dialog.open();
}

    private void openEditDialog(Grupo grupo) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        
        TextField nombreEdit = new TextField("Nombre");
        NumberField capacidadEdit = new NumberField("Capacidad");
        
        nombreEdit.setValue(grupo.getNombre());
        capacidadEdit.setValue((double) grupo.getCapacidadEstudiantes());
        
        formLayout.add(nombreEdit, capacidadEdit);
        
        Button saveButton = new Button("Guardar", event -> {
            if (validateEditFields(nombreEdit, capacidadEdit)) {
                grupo.setNombre(nombreEdit.getValue());
                grupo.setCapacidadEstudiantes(capacidadEdit.getValue().intValue());
                
                try {
                    controller.save(grupo);
                    refreshGrid();
                    dialog.close();
                    Notification.show("Grupo actualizado correctamente")
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                } catch (Exception e) {
                    Notification.show("Error: " + e.getMessage())
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            }
        });
        
        Button cancelButton = new Button("Cancelar", event -> dialog.close());
        
        dialog.add(new H3("Editar Grupo"));
        dialog.add(formLayout);
        dialog.add(new HorizontalLayout(saveButton, cancelButton));
        dialog.open();
    }

    private void showGrupoDetails(Grupo grupo) {
    Optional<Grupo> grupoConDetalles = controller.findByIdWithDetails(grupo.getId());

    Dialog dialog = new Dialog();
    VerticalLayout mainLayout = new VerticalLayout();
    
    // Información básica del grupo
    H3 title = new H3("Detalles del Grupo");
    FormLayout formLayout = new FormLayout();
    
    TextField nombreField = new TextField("Nombre");
    nombreField.setValue(grupo.getNombre());
    nombreField.setReadOnly(true);
    
    NumberField capacidadField = new NumberField("Capacidad");
    capacidadField.setValue((double) grupo.getCapacidadEstudiantes());
    capacidadField.setReadOnly(true);
    
    formLayout.add(nombreField, capacidadField);
    
    // Grid de estudiantes
    H4 estudiantesTitle = new H4("Estudiantes Asignados");
    Grid<Estudiante2> estudiantesGrid = new Grid<>(Estudiante2.class, false);
    estudiantesGrid.addColumn(Estudiante2::getNombre).setHeader("Nombre");
    estudiantesGrid.addColumn(Estudiante2::getApellido).setHeader("Apellido");
    estudiantesGrid.addColumn(Estudiante2::getCarnet).setHeader("Carnet");
    estudiantesGrid.setItems(grupo.getEstudiantes());
    estudiantesGrid.setHeight("200px");
    
    // Grid de horarios
    H4 horariosTitle = new H4("Horarios Asignados");
    Grid<Horario> horariosGrid = new Grid<>(Horario.class, false);
    horariosGrid.addColumn(horario -> horario.getPeriodo().getNombre()).setHeader("Periodo");
    horariosGrid.addColumn(horario -> horario.getMateria().getNombre()).setHeader("Materia");
    horariosGrid.addColumn(Horario::getAula).setHeader("Aula");
    horariosGrid.addColumn(horario -> 
        horario.getProfesor().getNombre() + " " + horario.getProfesor().getApellido())
        .setHeader("Profesor");
    horariosGrid.addColumn(Horario::getHoraInicio).setHeader("Hora Inicio");
    horariosGrid.addColumn(Horario::getHoraFin).setHeader("Hora Fin");
    horariosGrid.setItems(grupo.getHorarios());
    horariosGrid.setHeight("200px");
    
    // Botón de cerrar
    Button closeButton = new Button("Cerrar", e -> dialog.close());
    closeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    
    mainLayout.add(
        title,
        formLayout,
        estudiantesTitle,
        estudiantesGrid,
        horariosTitle,
        horariosGrid,
        closeButton
    );
    
    mainLayout.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    dialog.add(mainLayout);
    dialog.setWidth("800px");
    dialog.setHeight("800px");
    
    dialog.open();
}

    private boolean validateEditFields(TextField nombreEdit, NumberField capacidadEdit) {
        if (nombreEdit.isEmpty() || capacidadEdit.isEmpty()) {
            Notification.show("Los campos Nombre y Capacidad son requeridos")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        
        if (capacidadEdit.getValue() < 1) {
            Notification.show("La capacidad debe ser mayor a 0")
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return false;
        }
        
        return true;
    }
    
    private void deleteGrupo(Grupo grupo) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.add(new Text("¿Estás seguro de que deseas eliminar este grupo?"));
        
        Button confirmButton = new Button("Eliminar", event -> {
            try {
                controller.delete(grupo);
                refreshGrid();
                Notification.show("Grupo eliminado correctamente")
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                confirmDialog.close();
            } catch (Exception e) {
                Notification.show("Error: " + e.getMessage())
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        
        Button cancelButton = new Button("Cancelar", event -> confirmDialog.close());
        
        confirmDialog.add(new HorizontalLayout(confirmButton, cancelButton));
        confirmDialog.open();
    }
}