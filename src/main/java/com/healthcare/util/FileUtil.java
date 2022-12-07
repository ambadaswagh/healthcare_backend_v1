package com.healthcare.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class FileUtil {
	public static File excel2Pdf(Iterator<Row> rowIterator) throws IOException, DocumentException {
		Document iText_xls_2_pdf = new Document();
		String fileName = "tmp" + File.separator + UUID.randomUUID().toString() + ".pdf";
		PdfWriter.getInstance(iText_xls_2_pdf, new FileOutputStream(fileName));
		iText_xls_2_pdf.open();
		PdfPTable my_table = new PdfPTable(2);
		PdfPCell table_cell;
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next(); // Fetch CELL
				switch (cell.getCellType()) { // Identify CELL type
				case Cell.CELL_TYPE_STRING:
					table_cell = new PdfPCell(new Phrase(cell.getStringCellValue()));
					my_table.addCell(table_cell);
					break;
				}
			}
		}
		iText_xls_2_pdf.add(my_table);
		iText_xls_2_pdf.close();
		return new File(fileName);
	}

	public static String getPDFFileName() {
		File f = new File("tmp");
		if (!f.exists()) {
			f.mkdirs();
		}
		return "tmp" + File.separator + UUID.randomUUID().toString() + ".pdf";

	}
}
