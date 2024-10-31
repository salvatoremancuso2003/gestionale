package Servlet.Utente;

import Entity.FileEntity;
import Entity.Presenza;
import Entity.Richiesta;
import Entity.Utente;
import Utils.Utility;
import static Utils.Utility.estraiEccezione;
import static Utils.Utility.findOriginalExcelFile;
import static Utils.Utility.logfile;
import jakarta.persistence.TypedQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class EstraiExcel extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String dataExcel = request.getParameter("data");
        String userId = request.getParameter("userId");

        FileEntity file = findOriginalExcelFile();
        File originalFile = new File(file.getFilepath());
        String tempFilePath = "COPIA_" + originalFile.getName();
        File tempFile = new File(originalFile.getParent(), tempFilePath);

        if (!tempFile.exists()) {
            try {
                Files.copy(originalFile.toPath(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                logfile.severe(estraiEccezione(e));
            }
        }

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ITALY);
        LocalDate primoGiorno = LocalDate.parse(dataExcel + "-01");

        try (FileInputStream fis = new FileInputStream(tempFile); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);

            //cella nome azienda
            Row row1 = sheet.getRow(0);
            Cell cellNomeAzienda = row1.getCell(2);
            cellNomeAzienda.setCellValue("SmartOOP");

            //cella mensilità
            Cell mensilità = row1.getCell(36);
            DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ITALY);
            String mese = primoGiorno.format(italianFormatter);
            mese = mese.substring(0, 1).toUpperCase() + mese.substring(1).toLowerCase();
            Set<LocalDate> giorniFestivi = new HashSet<>();
            int anno = primoGiorno.getYear();
            giorniFestivi.add(LocalDate.of(anno, 1, 1));
            giorniFestivi.add(LocalDate.of(anno, 1, 6));
            giorniFestivi.add(LocalDate.of(anno, 4, 25));
            giorniFestivi.add(LocalDate.of(anno, 5, 1));
            giorniFestivi.add(LocalDate.of(anno, 6, 2));
            giorniFestivi.add(LocalDate.of(anno, 8, 15));
            giorniFestivi.add(LocalDate.of(anno, 11, 1));
            giorniFestivi.add(LocalDate.of(anno, 12, 8));
            giorniFestivi.add(LocalDate.of(anno, 12, 25));
            giorniFestivi.add(LocalDate.of(anno, 12, 26));

            try {
                Date pasquaDate = Utility.find(anno);
                LocalDate pasqua = pasquaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate pasquetta = pasqua.plusDays(1);

                giorniFestivi.add(pasqua);
                giorniFestivi.add(pasquetta);
            } catch (Utility.YearOutOfRangeException e) {
                e.printStackTrace();
            }

            mensilità.setCellValue(mese + " " + anno);

            Utente user = Utility.findUserById(Long.valueOf(userId));
            List<Presenza> presenze = Utility.findPresenzeByUserAndMonth(Long.valueOf(userId), primoGiorno);

            long millisecondiTotali = 0;

            for (Presenza p : presenze) {
                if (p.getEntrata() != null && p.getUscita() != null) {
                    long entrataMillis = p.getEntrata().getTime();
                    long uscitaMillis = p.getUscita().getTime();

                    long durataMillis = uscitaMillis - entrataMillis;

                    millisecondiTotali += durataMillis;
                }
            }

            long minutiTotali = millisecondiTotali / (1000 * 60);
            long oreIntere = minutiTotali / 60;
            long minutiRimanenti = minutiTotali % 60;

            String oreTotaliFormattate;

            if (minutiRimanenti < 30) {
                oreTotaliFormattate = oreIntere + ":30";
            } else {
                oreIntere++;
                oreTotaliFormattate = oreIntere + ":00";
            }

//            int oreTotali = Utility.tryParse(oreTotaliFormattate);
//            if(oreTotali < user.getOre_contratto()){
//                
//                Richiesta permesso = Utility.findRichiestaPermessoByPresenza(p);
//                
//            }
            System.out.println("Ore totali lavorate nel mese: " + oreTotaliFormattate);

            //cella id utente
            Row row4 = sheet.getRow(4);
            Cell idDipendente = row4.getCell(0);
            idDipendente.setCellValue(user.getId());

            //Cella dipendente
            Cell dipendente = row4.getCell(1);
            dipendente.setCellValue(user.getNome() + " " + user.getCognome());

            CellStyle weekendStyle = workbook.createCellStyle();
            Font redFont = workbook.createFont();
            redFont.setColor(IndexedColors.RED.getIndex());
            redFont.setFontHeightInPoints((short) 12);
            weekendStyle.setFont(redFont);
            weekendStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            weekendStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            weekendStyle.setAlignment(HorizontalAlignment.CENTER);
            weekendStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            weekendStyle.setBorderTop(BorderStyle.MEDIUM);
            weekendStyle.setBorderBottom(BorderStyle.MEDIUM);
            weekendStyle.setBorderLeft(BorderStyle.MEDIUM);
            weekendStyle.setBorderRight(BorderStyle.MEDIUM);

            CellStyle normalDayStyle = workbook.createCellStyle();
            normalDayStyle.setAlignment(HorizontalAlignment.CENTER);
            normalDayStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            normalDayStyle.setBorderTop(BorderStyle.MEDIUM);
            normalDayStyle.setBorderBottom(BorderStyle.MEDIUM);
            normalDayStyle.setBorderLeft(BorderStyle.MEDIUM);
            normalDayStyle.setBorderRight(BorderStyle.MEDIUM);

            int giorniNelMese = primoGiorno.lengthOfMonth();

            Row row4Data = sheet.getRow(3);
            if (row4Data == null) {
                row4Data = sheet.createRow(3);
            }

            double oreTotaliLavorate = 0;

            for (int i = 1; i <= giorniNelMese; i++) {
                LocalDate giorno = primoGiorno.withDayOfMonth(i);
                Row row5Data = sheet.getRow(4);
                if (row5Data == null) {
                    row5Data = sheet.createRow(4);
                }
                Cell cellOreLavorate = row5Data.getCell(i + 2);
                if (cellOreLavorate == null) {
                    cellOreLavorate = row5Data.createCell(i + 2);
                }

                //CICLO ORE GIORNALIERE 
                long oreLavorateGiornaliere = presenze.stream()
                        .filter(p -> {
                            LocalDate entrataDate = p.getEntrata() != null
                                    ? p.getEntrata().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null;
                            return entrataDate != null && entrataDate.equals(giorno);
                        })
                        .mapToLong(p -> {
                            if (p.getEntrata() != null && p.getUscita() != null) {
                                long entrataMillis = p.getEntrata().getTime();
                                long uscitaMillis = p.getUscita().getTime();
                                return uscitaMillis - entrataMillis;
                            }
                            return 0;
                        })
                        .sum();

                double oreLavorate = oreLavorateGiornaliere / 3600000.0;
                cellOreLavorate.setCellValue(oreLavorate);
                oreTotaliLavorate += oreLavorate;

                Cell cell = row4Data.getCell(i + 2);
                if (cell == null) {
                    cell = row4Data.createCell(i + 2);
                }
                cell.setCellValue(i);
                cell.setCellType(CellType.NUMERIC);

                //Cella ore totali
                Row rowOre = sheet.getRow(4);
                Cell oreTotali = rowOre.getCell(34);
                oreTotali.setCellValue(oreTotaliLavorate);

                //System.out.println("Ore totali lavorate nel mese: " + oreTotaliLavorate);
                if (giorno.getDayOfWeek() == DayOfWeek.SATURDAY || giorno.getDayOfWeek() == DayOfWeek.SUNDAY && !giorniFestivi.contains(giorno)) {
                    cell.setCellStyle(weekendStyle);
                } else if (giorniFestivi.contains(giorno)) {
                    cell.setCellStyle(weekendStyle);
                    int startingRowIndex = 14;
                    int festiviColumnIndex = 1;

                    List<LocalDate> giorniFestiviOrdinati = giorniFestivi.stream()
                            .sorted(Comparator.naturalOrder())
                            .collect(Collectors.toList());

                    for (LocalDate giornoFestivo : giorniFestiviOrdinati) {
                        if (giornoFestivo.getMonth() == primoGiorno.getMonth() && giornoFestivo.getYear() == primoGiorno.getYear()) {
                            Row festivoRow = sheet.getRow(startingRowIndex);
                            if (festivoRow == null) {
                                festivoRow = sheet.createRow(startingRowIndex);
                            }

                            Cell cellNote = festivoRow.getCell(festiviColumnIndex);
                            if (cellNote == null) {
                                cellNote = festivoRow.createCell(festiviColumnIndex);
                            }

                            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("d MMMM", Locale.ITALY);
                            String giornoFormat = giornoFestivo.format(formatter2);
                            cellNote.setCellValue("Festivo - " + giornoFormat);
                            cellNote.setCellStyle(weekendStyle);

                            startingRowIndex++;
                        }

                    }
                } else {
                    cell.setCellStyle(normalDayStyle);
                }
                // Logica per inserire le richieste di permesso in base al tipo
                List<Richiesta> richieste = Utility.findRichiestaByUserAndMonth2(Long.valueOf(userId), giorno); // Assicurati di ottenere le richieste per il giorno corrente

                for (Richiesta richiesta : richieste) {
                    LocalDate dataInizio = richiesta.getData_inizio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate dataFine = richiesta.getData_fine().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                    // Controlla se il giorno corrente rientra nel range di richiesta
                    if (!giorno.isBefore(dataInizio) && !giorno.isAfter(dataFine)) {
                        Row rowPermesso;
                        String tipoPermesso = richiesta.getTipo_permesso().getDescrizione();

                        if (tipoPermesso.equalsIgnoreCase("Malattia")) {
                            rowPermesso = sheet.getRow(6);
                        } else if (tipoPermesso.equalsIgnoreCase("Permesso_studio")) {
                            rowPermesso = sheet.getRow(7);
                        } else if (tipoPermesso.equalsIgnoreCase("Ferie")) {
                            rowPermesso = sheet.getRow(8);
                        } else {
                            continue;
                        }

                        int giornoDelMese = giorno.getDayOfMonth() + 2;

                        Cell cellPermesso = rowPermesso.getCell(giornoDelMese);
                        if (cellPermesso == null) {
                            cellPermesso = rowPermesso.createCell(giornoDelMese);
                        }

                        Date dataInizio2 = richiesta.getData_inizio();
                        Date dataFine2 = richiesta.getData_fine();

                        long differenzaInMillisecondi = dataFine2.getTime() - dataInizio2.getTime();

                        long oreDurata = TimeUnit.MILLISECONDS.toHours(differenzaInMillisecondi);

                        if (tipoPermesso.equalsIgnoreCase("Ferie")) {
                            cellPermesso.setCellValue(user.getOre_contratto());
                            Row row5Data2 = sheet.getRow(4);
                            Cell giorno3 = row5Data2.getCell(giornoDelMese);
                            giorno3.setCellType(CellType.NUMERIC);
                            giorno3.setCellValue(0.00);
                        } else if (tipoPermesso.equalsIgnoreCase("Malattia")) {
                            cellPermesso.setCellValue(user.getOre_contratto());
                            Row row5Data2 = sheet.getRow(4);
                            Cell giorno3 = row5Data2.getCell(giornoDelMese);
                            giorno3.setCellType(CellType.NUMERIC);
                            giorno3.setCellValue(0.00);
                        } else if (tipoPermesso.equalsIgnoreCase("Permesso_studio")) {
                            cellPermesso.setCellValue(oreDurata);
                        } else {
                            cellPermesso.setCellValue(oreDurata);
                        }

                        CellStyle permessoStyle = workbook.createCellStyle();
                        Font redFont2 = workbook.createFont();
                        redFont2.setColor(IndexedColors.RED.getIndex());
                        redFont2.setFontHeightInPoints((short) 12);
                        DataFormat format = workbook.createDataFormat();
                        permessoStyle.setDataFormat(format.getFormat("#,##0.00"));
                        permessoStyle.setFont(redFont2);
                        permessoStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
                        permessoStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        permessoStyle.setAlignment(HorizontalAlignment.CENTER);
                        permessoStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                        permessoStyle.setBorderTop(BorderStyle.THIN);
                        permessoStyle.setBorderBottom(BorderStyle.THIN);
                        permessoStyle.setBorderLeft(BorderStyle.THIN);
                        permessoStyle.setBorderRight(BorderStyle.THIN);
                        cellPermesso.setCellStyle(permessoStyle);
                    }
                }
            }

            for (int i = giorniNelMese + 1; i <= 31; i++) {
                Cell cell = row4Data.getCell(i + 2);
                if (cell != null) {
                    cell.setCellValue("");
                }
            }
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                workbook.write(fos);
            }
        }

        response.setContentType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader(
                "Content-Disposition", "attachment; filename=estrazione.xlsx");
        response.setContentLength(
                (int) tempFile.length()
        );

        try (FileInputStream fis = new FileInputStream(tempFile); ServletOutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
