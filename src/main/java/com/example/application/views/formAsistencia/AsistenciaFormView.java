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
import com.vaadin.flow.component.Composite;
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
import com.vaadin.flow.server.StreamResource;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@PageTitle("Educantrol - Asistencia")
@Route(value = "Asistencia", layout = MainLayout.class)
public class AsistenciaFormView extends Composite<VerticalLayout> {

    private final AsistenciaRepository asistenciaRepository;

    private ComboBox<Estudiante2> estudianteComboBox;
    private ComboBox<Periodo> periodoComboBox;
    private ComboBox<Grupo> grupoComboBox;
    private DatePicker fechaDatePicker;
    private RadioButtonGroup<String> estadoRadioGroup;
    private Button guardarButton;
    private Button buscarButton;  // Botón para buscar asistencia por carnet
    private Button exportarPdfButton;
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
        exportarPdfButton = new Button("Exportar a PDF");

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

        //Configurar el botón de exportar PDF
        exportarPdfButton.addClickListener(event -> exportarAsistenciaAPdf());

        // Configuración del Layout
        configurarLayout();
    }

    private void configurarLayout() {
        // Título
        H3 titulo = new H3("Gestión de Asistencia");
    
        // Layout principal que contendrá todo
        VerticalLayout layoutColumn2 = new VerticalLayout();
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
    
        // Configuración del contenedor principal
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);

        // Formulario
        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("100%");
        formLayout.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("500px", 2)
        );
    
        formLayout.add(
            estudianteComboBox,
            periodoComboBox,
            grupoComboBox,
            fechaDatePicker,
            estadoRadioGroup,
            guardarButton
        );

        // Layout de búsqueda
        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setAlignItems(Alignment.BASELINE);
        searchLayout.add(buscarCarnetField, buscarButton, exportarPdfButton);

        // Agregar todo al layout principal
        layoutColumn2.add(
         titulo,
         formLayout,
         searchLayout,
         grid
        );

        // Agregar el layout principal al contenido
        getContent().add(layoutColumn2);
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

    private void exportarAsistenciaAPdf() {
        // Capturar la referencia de UI actual antes de iniciar el hilo
        UI ui = UI.getCurrent();

        Notification notificationInicio = new Notification("Generando PDF...");
        notificationInicio.setDuration(2000);
        notificationInicio.open();

        // Crear un nuevo hilo para la generación del PDF
        Thread pdfThread = new Thread(() -> {
            try {
                // Se crea el archivo en la raíz del proyecto
                String fileName = "asistencias_" + LocalDate.now().toString() + ".pdf";
                File file = new File(fileName);
                OutputStream outputStream = new FileOutputStream(file);
    
                // Creación de documento PDF
                Document document = new Document();
                PdfWriter.getInstance(document, outputStream);
                document.open();
    
                Paragraph title = new Paragraph("Reporte de Asistencias");
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph("\n"));
    
                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
    
                table.addCell("Estudiante");
                table.addCell("Grupo");
                table.addCell("Periodo");
                table.addCell("Fecha");
                table.addCell("Estado");
    
                // Obtiene los records de asistencia
                List<Asistencia> asistencias = asistenciaRepository.findAll();
    
                for (Asistencia asistencia : asistencias) {
                    // Añade el estudiante
                    table.addCell(asistencia.getEstudiante() != null ? 
                        asistencia.getEstudiante().getNombre() + " " + asistencia.getEstudiante().getApellido() : 
                        "No disponible");
    
                    // Añade el grupo
                    table.addCell(asistencia.getGrupo() != null ? 
                        asistencia.getGrupo().getNombre() : 
                        "No disponible");
    
                    // Añade el periodo
                    table.addCell(asistencia.getPeriodo() != null ? 
                        asistencia.getPeriodo().getNombre() : 
                        "No disponible");
    
                    // Añade la fecha
                    table.addCell(asistencia.getFecha().toString());
    
                    // Añade el estado de la asistencia
                    table.addCell(asistencia.isPresente() ? "Presente" : "Ausente");
                }
    
                document.add(table);
                document.close();
                outputStream.close();
    
                
            // Usar la referencia de UI capturada
            ui.access(() -> {
                try {
                    // Mostrar notificación de éxito
                    Notification notificationExito = new Notification(
                        "PDF generado exitosamente", 
                        3000, 
                        Notification.Position.BOTTOM_START
                    );
                    notificationExito.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notificationExito.open();

                    // Abrir el PDF
                    String filePath = file.getAbsolutePath();
                    String fileUrl = "file:///" + filePath.replace("\\", "/");
                    ui.getPage().open(fileUrl, "_blank");
                } catch (Exception e) {
                    Notification.show("Error al abrir el PDF: " + e.getMessage());
                }
            });

        } catch (Exception e) {
            ui.access(() -> {
                Notification notificationError = new Notification(
                    "Error al generar el PDF: " + e.getMessage(),
                    3000,
                    Notification.Position.BOTTOM_START
                );
                notificationError.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notificationError.open();
            });
        }
    });

    pdfThread.start();
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
