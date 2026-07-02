package com.example.oilbilling.services;

import com.example.oilbilling.DTO.InvoiceViewHeaderDTO;
import com.example.oilbilling.DTO.InvoiceViewItemsDTO;
import com.example.oilbilling.DTO.InvoiceViewResponseDTO;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
@Service
public class PdGenerationService {

    public ByteArrayOutputStream generateInvoicePDF (InvoiceViewResponseDTO invoiceViewResponseDTO) throws IOException {
        PDDocument document= new PDDocument();
        PDPage page= new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

// LOGO
        PDImageXObject logo=PDImageXObject.createFromFile("src/main/resources/logo.jpg", document);
        contentStream.drawImage(logo, 50, 750, 100, 50);

        contentStream.setFont(PDType1Font.HELVETICA_BOLD,20);
        contentStream.beginText();
        contentStream.newLineAtOffset(200,700);
        contentStream.showText("INVOICE");
        contentStream.endText();

        InvoiceViewHeaderDTO header=  invoiceViewResponseDTO.getInvoiceViewHeaderDTO();
        contentStream.setFont(PDType1Font.HELVETICA,12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50,720);
        contentStream.showText("Invoice No: "+header.getInvoiceID());
        contentStream.newLineAtOffset(0,-15);
        contentStream.showText("Date: "+ LocalDate.now());
        contentStream.newLineAtOffset(0,-15);
        contentStream.showText("Customer Name: "+header.getCustomerName());
        contentStream.endText();

        float startX= 50;
        float startY=650;
        float rowHeight=20;

        float itemNameX= startX;
        float quantityX=300;
        float subTotalX=420;

        float pageWidth = page.getMediaBox().getWidth(); // ~595
        float startBottomFieldsX = pageWidth - 200; // right aligned
        float startBottomFieldsY = 120;

        contentStream.setFont(PDType1Font.HELVETICA,12);
        contentStream.beginText();
        contentStream.newLineAtOffset(itemNameX,startY);
        contentStream.showText("Item Name");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(quantityX,startY);
        contentStream.showText("Quantity");
        contentStream.endText();

        contentStream.beginText();
        contentStream.newLineAtOffset(subTotalX, startY);
        contentStream.showText("Sub Total");
        contentStream.endText();

        contentStream.moveTo(startX,startY -5);
        contentStream.lineTo(550,startY -5);
        contentStream.stroke();

        contentStream.setFont(PDType1Font.HELVETICA,11);
        float currentY = startY - rowHeight;
        List<InvoiceViewItemsDTO> items =invoiceViewResponseDTO.getInvoiceViewItemsDTO();
        for (InvoiceViewItemsDTO item: items ) {

            contentStream.beginText();
            contentStream.newLineAtOffset(itemNameX, currentY);
            contentStream.showText(item.getItemName());
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(quantityX,currentY);
            contentStream.showText(String.valueOf(item.getItemQuantity()));
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(subTotalX,currentY);
            contentStream.showText(String.valueOf(item.getItemPrice()));
            contentStream.endText();

            contentStream.moveTo(startX, currentY - 5);
            contentStream.lineTo(550, currentY - 5);
            contentStream.stroke();

            currentY -= rowHeight;
        }

        contentStream.setFont(PDType1Font.HELVETICA_BOLD,12);
        contentStream.beginText();
        contentStream.newLineAtOffset(startBottomFieldsX,startBottomFieldsY);
        contentStream.showText("gst : " +header.getGst());
        contentStream.endText();
        contentStream.beginText();
        contentStream.newLineAtOffset(startBottomFieldsX,startBottomFieldsY-20);
        contentStream.showText("Grand Total : " +header.getGrandTotal());
        contentStream.endText();


        contentStream.beginText();
        contentStream.newLineAtOffset(50, 50);
        contentStream.showText("Oil Billing Pvt. Ltd.");
        contentStream.newLineAtOffset(0, -12);
        contentStream.showText("#123, Industrial Area, Shivamogga, Karantaka");
        contentStream.newLineAtOffset(0, -12);
        contentStream.showText("Phone: 7411748408 | Email: info@oil.com");
        contentStream.endText();

        contentStream.close();

        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        return outputStream;
    }
}
