package com.example.application.views.formAsistencia;

import com.example.application.modelo.Asistencia;
import com.example.application.modelo.AsistenciaRepository;
import com.example.application.modelo.Estudiante2;
import com.example.application.modelo.Estudiante2Repository;
import com.example.application.modelo.Periodo;
import com.example.application.modelo.PeriodoRepository;
import com.example.application.modelo.Grupo;
import com.example.application.modelo.GrupoRepository;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;

import java.time.LocalDate;
import java.util.List;

@PageTitle("Educantrol - Asistencia")
@Route(value = "Asistencia", layout = MainLayout.class)
public class AsistenciaFormView extends VerticalLayout {

    private final AsistenciaRepository asistenciaRepository;

    private ComboBox<Estudiante2> estudianteComboBox;
    private ComboBox<Periodo> periodoComboBox;
    private ComboBox<Grupo> grupoComboBox;
    private DatePicker fechaDatePicker;
    private RadioButtonGroup<String> estadoRadioGroup;
    private Button guardarButton;
    private Button buscarButton;  // Botón para buscar asistencia por carnet
    private TextField buscarCarnetField;  // Campo para ingresar el número de carnet
    private Grid<Asistencia> grid;

    public AsistenciaFormView(AsistenciaRepository asistenciaRepository, 
                              Estudiante2Repository estudiante2Repository, 
                              PeriodoRepository periodoRepository,
                              GrupoRepository grupoRepository) {
        this.asistenciaRepository = asistenciaRepository;

        // Iniciar los componentes de la vista
        estudianteComboBox = new ComboBox<>("Seleccionar Estudiante");
        periodoComboBox = new ComboBox<>("Seleccionar Periodo");
        grupoComboBox = new ComboBox<>("Seleccionar Grupo");
        fechaDatePicker = new DatePicker("Fecha de Asistencia");
        estadoRadioGroup = new RadioButtonGroup<>();
        guardarButton = new Button("Guardar");
        buscarCarnetField = new TextField("Buscar por Carnet");
        buscarButton = new Button("Buscar");

        grid = new Grid<>();

        // Configurar los ComboBox con los estudiantes, periodos y grupos
        estudianteComboBox.setItems(estudiante2Repository.findAll());
        estudianteComboBox.setItemLabelGenerator(estudiante -> 
            estudiante.getNombre() + " " + estudiante.getApellido() + " (" + estudiante.getCarnet() + ")");
        periodoComboBox.setItems(periodoRepository.findAll());
        periodoComboBox.setItemLabelGenerator(Periodo::getNombre);
        grupoComboBox.setItems(grupoRepository.findAll());
        grupoComboBox.setItemLabelGenerator(Grupo::getNombre);

        // Configurar el estado de la asistencia
        estadoRadioGroup.setLabel("Estado de Asistencia");
        estadoRadioGroup.setItems("Presente", "Ausente");

        // Establecer la fecha por defecto al día de hoy
        fechaDatePicker.setValue(LocalDate.now());

        // Configurar el botón de guardar
        guardarButton.addClickListener(event -> guardarAsistencia());

        // Configurar el botón de búsqueda
        buscarButton.addClickListener(event -> buscarAsistenciasPorCarnet());

        // Configuración del Layout
        configurarLayout();
    }

    private void configurarLayout() {
        // Título
        H3 titulo = new H3("Control de Asistencia");

        // Formulario
        FormLayout formLayout = new FormLayout();
        formLayout.add(
            estudianteComboBox,
            periodoComboBox,
            grupoComboBox,
            fechaDatePicker,
            estadoRadioGroup,
            guardarButton
        );

        // Agregar campo y botón para buscar asistencia por carnet
        VerticalLayout searchLayout = new VerticalLayout();
        searchLayout.add(buscarCarnetField, buscarButton);

        // Layout principal
        VerticalLayout mainLayout = new VerticalLayout(titulo, formLayout, searchLayout, grid);
        mainLayout.setWidth("100%");
        mainLayout.setHeightFull();
        mainLayout.setAlignItems(Alignment.CENTER); // Centrar los elementos en el layout
        mainLayout.setPadding(true);  // Añadir padding alrededor del formulario
        mainLayout.setSpacing(true);  // Añadir espacio entre los componentes

        // Agregar el layout principal al contenido
        add(mainLayout);
    }

    private void guardarAsistencia() {
        // Obtener los valores de los campos
        Estudiante2 estudiante = estudianteComboBox.getValue();
        Periodo periodo = periodoComboBox.getValue();
        Grupo grupo = grupoComboBox.getValue();
        LocalDate fecha = fechaDatePicker.getValue();
        String estado = estadoRadioGroup.getValue();

        // Validaciones
        if (estado == null) {
            Notification notification = new Notification("Por favor, selecciona el estado de la asistencia." );
            notification.setDuration(2000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);  // Error
            notification.open();
            return;
        }

        if (periodo == null) {
            Notification notification = new Notification("Por favor, selecciona un periodo.");
            notification.setDuration(2000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);  // Error
            notification.open();
            return;
        }

        if (grupo == null) {
            Notification notification = new Notification("Por favor, selecciona un grupo.");
            notification.setDuration(2000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);  // Error
            notification.open();
            return;
        }

        if (estudiante == null) {
            Notification notification = new Notification("Por favor, selecciona un estudiante.");
            notification.setDuration(2000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);  // Error
            notification.open();
            return;
        }

        if (fecha == null) {
            Notification notification = new Notification("Por favor, selecciona la fecha de asistencia.");
            notification.setDuration(2000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);  // Error
            notification.open();
            return;
        }


        // Validación: Verificar si el estudiante ya tiene asistencia registrada para el mismo grupo, periodo y fecha
        List<Asistencia> asistenciasExistentes = asistenciaRepository.findByEstudianteAndGrupoAndPeriodoAndFecha(estudiante, grupo, periodo, fecha);
        if (asistenciasExistentes.size() >= 1) {
            Notification notification = new Notification("Este estudiante ya tiene registros de asistencia para este grupo y periodo en este día.");
            notification.setDuration(2000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);  // Error
            notification.open();
            return;
        }

        // Convertir el estado en un valor booleano
        boolean presente = estado.equals("Presente");

        // Crear un nuevo objeto de Asistencia
        Asistencia asistencia = new Asistencia(estudiante, grupo, periodo, fecha, presente);

        // Guardar la asistencia en la base de datos
        try {
            asistenciaRepository.save(asistencia);
            Notification notification = new Notification("Asistencia registrada correctamente.");
            notification.setDuration(2000);
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);  // Éxito
            notification.open();
            limpiarFormulario();
            actualizarGrid(); // Actualizar el grid después de guardar
        } catch (Exception e) {
            Notification notification = new Notification("Ocurrió un error al guardar la asistencia: " + e.getMessage());
            notification.setDuration(2000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);  // Error
            notification.open();
        }
    }

    private void limpiarFormulario() {
        estudianteComboBox.clear();
        periodoComboBox.clear();
        grupoComboBox.clear();
        estadoRadioGroup.clear();
        fechaDatePicker.setValue(LocalDate.now());
    }

    private void actualizarGrid() {
        grid.setItems(asistenciaRepository.findAll());
    }

    private void buscarAsistenciasPorCarnet() {
        String carnet = buscarCarnetField.getValue().trim();

        if (!carnet.isEmpty()) {
            List<Asistencia> asistencias = asistenciaRepository.findByEstudianteCarnet(carnet);
            grid.setItems(asistencias);
        } else {
            Notification notification = new Notification("Por favor, ingresa un número de carnet para realizar la búsqueda.");
            notification.setDuration(2000);
            notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            notification.open();
        }
    }

    private void configurarGrid() {
        grid.addColumn(asistencia -> asistencia.getEstudiante() != null
                ? asistencia.getEstudiante().getNombre() + " " + asistencia.getEstudiante().getApellido()
                : "No disponible")
            .setHeader("Estudiante")
            .setSortable(true);

        grid.addColumn(asistencia -> asistencia.getGrupo() != null
                ? asistencia.getGrupo().getNombre()
                : "No disponible")
            .setHeader("Grupo")
            .setSortable(true);

        grid.addColumn(asistencia -> asistencia.getPeriodo() != null 
                ? asistencia.getPeriodo().getNombre()
                : "No disponible")
            .setHeader("Periodo")
            .setSortable(true);

        grid.addColumn(Asistencia::getFecha)
            .setHeader("Fecha")
            .setSortable(true);

        // Cambiar color de la celda basado en el estado
        grid.addColumn(new ComponentRenderer<>(asistencia -> {
            Div div = new Div();
            if (asistencia.isPresente()) {
                div.setText("Presente");
                div.getStyle().set("color", "green");
            } else {
                div.setText("Ausente");
                div.getStyle().set("color", "red");
            }
            return div;
        })).setHeader("Estado")
          .setSortable(true);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        configurarGrid();
        actualizarGrid();
    }
}
