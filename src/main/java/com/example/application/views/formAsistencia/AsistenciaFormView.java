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
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.UI;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;

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
    
        // Crear contenedor para el grid que ocupe el 100% de la ventana
        Div gridContainer = new Div(grid);
        gridContainer.setWidthFull(); // Asegura que el contenedor ocupe todo el ancho disponible
        grid.setWidthFull(); // Esto hace que el Grid ocupe todo el ancho disponible
    
        // Layout de búsqueda
        HorizontalLayout searchLayout = new HorizontalLayout();
        searchLayout.setAlignItems(Alignment.BASELINE);
        searchLayout.add(buscarCarnetField, buscarButton, exportarPdfButton);
    
        // Agregar todo al layout principal
        layoutColumn2.add(
            titulo,
            formLayout,
            searchLayout,
            gridContainer // El Grid ocupará el espacio restante
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
    
    private void editarAsistencia(Asistencia asistencia) {
        // Crear un diálogo de confirmación
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Confirmación de edición");
    
        // Mensaje dentro del diálogo
        @SuppressWarnings({ "deprecation",})
        Label mensaje = new Label("¿Estás seguro de que deseas editar esta asistencia?");
        confirmDialog.add(mensaje);
    
        // Botón para confirmar la edición
        Button confirmarButton = new Button("Continuar", event -> {
            // Llenar el formulario con los datos de la asistencia seleccionada
            estudianteComboBox.setValue(asistencia.getEstudiante());
            periodoComboBox.setValue(asistencia.getPeriodo());
            grupoComboBox.setValue(asistencia.getGrupo());
            fechaDatePicker.setValue(asistencia.getFecha());
            estadoRadioGroup.setValue(asistencia.isPresente() ? "Presente" : "Ausente");
    
            // Cambiar el texto del botón "Guardar" a "Actualizar"
            guardarButton.setText("Actualizar");
    
            // Añadir el evento de clic para actualizar la asistencia
            guardarButton.addClickListener(guardarEvent -> {
                // Validaciones necesarias antes de actualizar
                if (validarFormulario()) {
                    asistencia.setEstudiante(estudianteComboBox.getValue());
                    asistencia.setPeriodo(periodoComboBox.getValue());
                    asistencia.setGrupo(grupoComboBox.getValue());
                    asistencia.setFecha(fechaDatePicker.getValue());
                    asistencia.setPresente(estadoRadioGroup.getValue().equals("Presente"));
    
                    try {
                        asistenciaRepository.save(asistencia);
                        Notification.show("Asistencia actualizada con éxito.");
                        limpiarFormulario();
                        actualizarGrid();
                        guardarButton.setText("Guardar"); // Volver a su estado original
                    } catch (Exception e) {
                        Notification.show("Error al actualizar la asistencia: " + e.getMessage());
                    }
                }
            });
    
            confirmDialog.close();
        });
        confirmarButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    
        // Botón para cancelar la acción
        Button cancelarButton = new Button("Cancelar", event -> confirmDialog.close());
        cancelarButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    
        // Contenedor para los botones
        HorizontalLayout buttonsLayout = new HorizontalLayout(confirmarButton, cancelarButton);
        buttonsLayout.setJustifyContentMode(JustifyContentMode.END);
    
        // Añadir los botones al diálogo
        confirmDialog.getFooter().add(buttonsLayout);
    
        // Abrir el diálogo
        confirmDialog.open();
    }
    
    
private void borrarAsistencia(Asistencia asistencia) {
    // Crear un diálogo de confirmación
    Dialog confirmDialog = new Dialog();
    confirmDialog.setHeaderTitle("Confirmación de eliminación");

    // Mensaje dentro del diálogo
    @SuppressWarnings("deprecation")
    Label mensaje = new Label("¿Estás seguro de que deseas eliminar esta asistencia?");
    confirmDialog.add(mensaje);

    // Botón para confirmar la eliminación
    Button confirmarButton = new Button("Eliminar", event -> {
        try {
            asistenciaRepository.delete(asistencia);
            Notification.show("Asistencia eliminada con éxito.");
            actualizarGrid();
        } catch (Exception e) {
            Notification.show("Error al eliminar la asistencia: " + e.getMessage());
        } finally {
            confirmDialog.close();
        }
    });
    confirmarButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

    // Botón para cancelar la acción
    Button cancelarButton = new Button("Cancelar", event -> confirmDialog.close());
    cancelarButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

    // Contenedor para los botones
    HorizontalLayout buttonsLayout = new HorizontalLayout(confirmarButton, cancelarButton);
    buttonsLayout.setJustifyContentMode(JustifyContentMode.END);

    // Añadir los botones al diálogo
    confirmDialog.getFooter().add(buttonsLayout);

    // Abrir el diálogo
    confirmDialog.open();
}

    
    // Validar formulario
    private boolean validarFormulario() {
        if (estudianteComboBox.isEmpty()) {
            Notification.show("Selecciona un estudiante.");
            return false;
        }
        if (periodoComboBox.isEmpty()) {
            Notification.show("Selecciona un periodo.");
            return false;
        }
        if (grupoComboBox.isEmpty()) {
            Notification.show("Selecciona un grupo.");
            return false;
        }
        if (fechaDatePicker.isEmpty()) {
            Notification.show("Selecciona una fecha.");
            return false;
        }
        if (estadoRadioGroup.isEmpty()) {
            Notification.show("Selecciona el estado de la asistencia.");
            return false;
        }
        return true;
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
                // Obtener la ruta del directorio de descargas del usuario
                String userHome = System.getProperty("user.home");
                String downloadsPath = userHome + "/Downloads"; // Esto funciona en la mayoría de los sistemas operativos
                File downloadDir = new File(downloadsPath);
    
                if (!downloadDir.exists()) {
                    downloadDir.mkdirs();  // Crear el directorio si no existe
                }
    
                // Crear el archivo PDF en el directorio de descargas
                String fileName = "asistencias_" + LocalDate.now().toString() + ".pdf";
                File file = new File(downloadsPath, fileName);
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
    
                // Obtiene los registros de asistencia
                List<Asistencia> asistencias = asistenciaRepository.findAll();
    
                for (Asistencia asistencia : asistencias) {
                    table.addCell(asistencia.getEstudiante() != null 
                        ? asistencia.getEstudiante().getNombre() + " " + asistencia.getEstudiante().getApellido() 
                        : "No disponible");
    
                    table.addCell(asistencia.getGrupo() != null 
                        ? asistencia.getGrupo().getNombre() 
                        : "No disponible");
    
                    table.addCell(asistencia.getPeriodo() != null 
                        ? asistencia.getPeriodo().getNombre() 
                        : "No disponible");
    
                    table.addCell(asistencia.getFecha().toString());
    
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
    
        // Columna para editar
        grid.addColumn(new ComponentRenderer<>(asistencia -> {
            Button editButton = new Button("Editar");
            editButton.addClickListener(event -> editarAsistencia(asistencia));
                        return editButton;
                    })).setHeader("Editar");
                
                    // Columna para borrar
                    grid.addColumn(new ComponentRenderer<>(asistencia -> {
                        Button deleteButton = new Button("Borrar");
                        deleteButton.getStyle().set("color", "red");
                        deleteButton.addClickListener(event -> borrarAsistencia(asistencia));
                                    return deleteButton;
                                })).setHeader("Borrar");
                            }
            
                @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        configurarGrid();
        actualizarGrid();
    }
}
