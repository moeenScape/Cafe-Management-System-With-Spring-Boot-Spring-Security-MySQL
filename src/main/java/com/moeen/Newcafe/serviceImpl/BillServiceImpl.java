package com.moeen.Newcafe.serviceImpl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.moeen.Newcafe.Entity.Bill;
import com.moeen.Newcafe.Entity.User;
import com.moeen.Newcafe.JWT.JwtFilter;
import com.moeen.Newcafe.Utils.CafeUtils;
import com.moeen.Newcafe.constents.CafeConstent;
import com.moeen.Newcafe.dao.BillDao;
import com.moeen.Newcafe.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static com.itextpdf.text.FontFactory.getFont;

@Slf4j
@Service
public class BillServiceImpl implements BillService {
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    BillDao billDao;
    @Override
    public ResponseEntity<String> generateBill(Map<String, Object> requestMap) {
        log.info("In R");
        try{
            String filename;
            if(validateRequestMap(requestMap))
            {
                if(requestMap.containsKey("isGenerate")&&!(Boolean)requestMap.get("isGenerate"))
                {
                    filename=(String) requestMap.get("uuid");
                }
                else {
                    filename=CafeUtils.getUUID();
                    requestMap.put("uuid",filename);
                    inputBill(requestMap);

                }
                String data= "Name: "+requestMap.get("name")+"\n"+"Contract Number"+requestMap.get("contractNumber")+"\n"
                        +"Email: "+requestMap.get("email")+"\n"+"PaymentMethod: "+requestMap.get("paymentMethod");

                Document document=new Document();
                PdfWriter.getInstance(document,new FileOutputStream(CafeConstent.STORE_LOCATION+"\\"+filename+".pdf"));

                document.open();
                setRectangleInPdf(document);

                Paragraph chunk=new Paragraph("SIMS System",getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph=new Paragraph(data+"\n \n",getFont("Data"));
                document.add(paragraph);

                PdfPTable table=new PdfPTable(5);
                table.setWidthPercentage(100);
                addTableHeader(table);


                JSONArray jsonArray=CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
                for(int i=0;i<jsonArray.length();i++)
                {
                    addRow(table,CafeUtils.getMapFromJson(jsonArray.getString(i)));

                }
                document.add(table);

                Paragraph footer=new Paragraph("Total: "+requestMap.get("totalAmount")+"\n"+
                        "Thanks",getFont("Data"));

                document.add(footer);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\""+filename+"\"}",HttpStatus.CREATED);

            }else {
                return CafeUtils.getResponse("Required Data not Found", HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        List<Bill> list=new ArrayList<>();
        if(jwtFilter.isAdmin())
        {
            list=billDao.getAllBills();
        }else {
            list=billDao.getBillsByName(jwtFilter.getCurrentUser());
        }

        return new ResponseEntity<>(list,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<byte[]> getPDF(Map<String, Object> requestMap) {
        log.info("Inside Get PDF{}",requestMap);
        try{
            byte[] byteArray=new byte[0];
            log.info("inside Try GetPDf");
            if(!requestMap.containsKey("uuid") && validateRequestMap(requestMap))
            {
                //log.info("inside if GetPDf");
                return new ResponseEntity<>(byteArray,HttpStatus.BAD_REQUEST);
            }
           else {
                //log.info("inside else GetPDf");
                String filePath = CafeConstent.STORE_LOCATION+"\\"+(String) requestMap.get("uuid")+".pdf";
                if(CafeUtils.isFileExist(filePath))
                {
                   // log.info("inside else if GetPDf");
                    byteArray=getByteArray(filePath);
                    return new ResponseEntity<>(byteArray,HttpStatus.OK);
                }else {
                    requestMap.put("isGenerate",false);
                    generateBill(requestMap);
                }
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBillById(Integer id) {
        try{
            Optional<Bill> optional=billDao.findById(id);
            if(!optional.isEmpty())
            {
                billDao.deleteById(id);
                return CafeUtils.getResponse("Delete Bill Successfully",HttpStatus.OK);
            }else{
                return CafeUtils.getResponse("Bill Id Does not exist",HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    private byte[] getByteArray(String filePath) throws Exception {
        //log.info("Inside byteArray {}",filePath);
        File initialFile=new File(filePath);
        InputStream targetStream=new FileInputStream(initialFile);
        byte[] byteArray= IOUtils.toByteArray(targetStream);
        targetStream.close();
        return byteArray;
    }


    private void addRow(PdfPTable table, Map<String, Object> mapFromJson) {
        log.info("Add inside Row");
        table.addCell((String) mapFromJson.get("name"));
        table.addCell((String) mapFromJson.get("category"));
        table.addCell((String) mapFromJson.get("quantity"));
        table.addCell(Double.toString((Double) mapFromJson.get("price")));
        table.addCell(Double.toString((Double) mapFromJson.get("total")));
    }

    private void addTableHeader(PdfPTable table) {
        log.info("Inside Table Header add");
        Stream.of("Name","category","Quantity","Price","Sub Total")
                .forEach(columTitle->{
                    PdfPCell header=new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columTitle));
                    header.setBackgroundColor(BaseColor.YELLOW);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont(String type)
    {
        log.info("Inside getFont");
        switch (type){
            case "Header":
                Font headerFont=FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE,18,BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont=FontFactory.getFont(FontFactory.TIMES_ROMAN,11,BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();

        }
    }


    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside Pdf");
        Rectangle rectangle=new Rectangle(500,800,18,15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBackgroundColor(BaseColor.WHITE);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }
    private void inputBill(Map<String, Object> requestMap) {
        try{
            Bill bill=new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContractNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
            bill.setProductDetails((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billDao.save(bill);
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> requestMap) {
        if(requestMap.containsKey("fileName")||requestMap.containsKey("name")|| requestMap.containsKey("contractNumber")
                ||requestMap.containsKey("email")|| requestMap.containsKey("paymentMethod")
                ||requestMap.containsKey("productDetails")||requestMap.containsKey("totalAmount"))
        {
            return true;
        }

        return false;
    }

}
