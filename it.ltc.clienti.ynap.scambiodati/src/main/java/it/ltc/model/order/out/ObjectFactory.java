//
// Questo file è stato generato dall'architettura JavaTM per XML Binding (JAXB) Reference Implementation, v2.2.8-b130911.1802 
// Vedere <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Qualsiasi modifica a questo file andrà persa durante la ricompilazione dello schema di origine. 
// Generato il: 2016.07.20 alle 11:28:44 AM CEST 
//


package it.ltc.model.order.out;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.ltc.model.order.out package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _POITMicro_QNAME = new QName("", "PO_IT_Micro");
    private final static QName _ITINVPrice_QNAME = new QName("", "IT_INV_Price");
    private final static QName _SHIPReqServLev_QNAME = new QName("", "SHIP_ReqServLev");
    private final static QName _INVCreditDeduction_QNAME = new QName("", "INV_CreditDeduction");
    private final static QName _SHIPDeliveryDate_QNAME = new QName("", "SHIP_DeliveryDate");
    private final static QName _SHIPPaymentType_QNAME = new QName("", "SHIP_PaymentType");
    private final static QName _ORDNum_QNAME = new QName("", "ORD_Num");
    private final static QName _RECAddr1_QNAME = new QName("", "REC_Addr1");
    private final static QName _RECAddr2_QNAME = new QName("", "REC_Addr2");
    private final static QName _ITMadeIn_QNAME = new QName("", "IT_MadeIn");
    private final static QName _ITDisc_QNAME = new QName("", "IT_Disc");
    private final static QName _ITJwGold375_QNAME = new QName("", "IT_Jw_Gold375");
    private final static QName _ORDRef_QNAME = new QName("", "ORD_Ref");
    private final static QName _INVDisc_QNAME = new QName("", "INV_Disc");
    private final static QName _RECEmail_QNAME = new QName("", "REC_Email");
    private final static QName _POITMacro_QNAME = new QName("", "PO_IT_Macro");
    private final static QName _INVGW_QNAME = new QName("", "INV_GW");
    private final static QName _RECProv_QNAME = new QName("", "REC_Prov");
    private final static QName _ITCompTypeID_QNAME = new QName("", "IT_CompType_ID");
    private final static QName _POITCustoms_QNAME = new QName("", "PO_IT_Customs");
    private final static QName _RECCity_QNAME = new QName("", "REC_City");
    private final static QName _POITColor_QNAME = new QName("", "PO_IT_Color");
    private final static QName _ITINVFreight_QNAME = new QName("", "IT_INV_Freight");
    private final static QName _ORDDate_QNAME = new QName("", "ORD_Date");
    private final static QName _RECPhone_QNAME = new QName("", "REC_Phone");
    private final static QName _SHIPDeliveryTime_QNAME = new QName("", "SHIP_DeliveryTime");
    private final static QName _POITExpectedShipDate_QNAME = new QName("", "PO_IT_ExpectedShipDate");
    private final static QName _SHIPFromID_QNAME = new QName("", "SHIP_From_ID");
    private final static QName _ITJwItemSerialNumber_QNAME = new QName("", "IT_Jw_ItemSerialNumber");
    private final static QName _ITArtSti_QNAME = new QName("", "IT_ArtSti");
    private final static QName _ITMarchio_QNAME = new QName("", "IT_Marchio");
    private final static QName _STZIP_QNAME = new QName("", "ST_ZIP");
    private final static QName _ITTransferPrice_QNAME = new QName("", "IT_TransferPrice");
    private final static QName _POITPrice_QNAME = new QName("", "PO_IT_Price");
    private final static QName _ITTransferPriceCurrISO_QNAME = new QName("", "IT_TransferPriceCurrISO");
    private final static QName _ITSTAddr2_QNAME = new QName("", "IT_ST_Addr2");
    private final static QName _ITSTAddr1_QNAME = new QName("", "IT_ST_Addr1");
    private final static QName _ORDBoxType_QNAME = new QName("", "ORD_BoxType");
    private final static QName _POITCodice10_QNAME = new QName("", "PO_IT_Codice10");
    private final static QName _SHIPToID_QNAME = new QName("", "SHIP_To_ID");
    private final static QName _ORDGW_QNAME = new QName("", "ORD_GW");
    private final static QName _ITJwSilver_QNAME = new QName("", "IT_Jw_Silver");
    private final static QName _ITCustomerOrderID_QNAME = new QName("", "IT_CustomerOrderID");
    private final static QName _INVCODCharge_QNAME = new QName("", "INV_CODCharge");
    private final static QName _RECMess_QNAME = new QName("", "REC_Mess");
    private final static QName _POITMarchio_QNAME = new QName("", "PO_IT_Marchio");
    private final static QName _ITINVCODCharge_QNAME = new QName("", "IT_INV_CODCharge");
    private final static QName _ITPrice_QNAME = new QName("", "IT_Price");
    private final static QName _ITSex_QNAME = new QName("", "IT_Sex");
    private final static QName _ITSTPhone_QNAME = new QName("", "IT_ST_Phone");
    private final static QName _POITSize_QNAME = new QName("", "PO_IT_Size");
    private final static QName _ORDNote_QNAME = new QName("", "ORD_Note");
    private final static QName _RECCountryName_QNAME = new QName("", "REC_CountryName");
    private final static QName _POITComp_QNAME = new QName("", "PO_IT_Comp");
    private final static QName _RECShortName_QNAME = new QName("", "REC_ShortName");
    private final static QName _ITGadget_QNAME = new QName("", "IT_Gadget");
    private final static QName _ITCustoms_QNAME = new QName("", "IT_Customs");
    private final static QName _STAddr1_QNAME = new QName("", "ST_Addr1");
    private final static QName _ITType_QNAME = new QName("", "IT_Type");
    private final static QName _STAddr2_QNAME = new QName("", "ST_Addr2");
    private final static QName _RECCountryCode_QNAME = new QName("", "REC_CountryCode");
    private final static QName _STName_QNAME = new QName("", "ST_Name");
    private final static QName _INVInsurance_QNAME = new QName("", "INV_Insurance");
    private final static QName _RECID_QNAME = new QName("", "REC_ID");
    private final static QName _ITPriorityLevel_QNAME = new QName("", "IT_PriorityLevel");
    private final static QName _STCountryCode_QNAME = new QName("", "ST_CountryCode");
    private final static QName _POITMadeIn_QNAME = new QName("", "PO_IT_MadeIn");
    private final static QName _ITLinkPhoto_QNAME = new QName("", "IT_LinkPhoto");
    private final static QName _POITNote_QNAME = new QName("", "PO_IT_Note");
    private final static QName _ITComp_QNAME = new QName("", "IT_Comp");
    private final static QName _RECPhone2_QNAME = new QName("", "REC_Phone2");
    private final static QName _SHIPResidential_QNAME = new QName("", "SHIP_Residential");
    private final static QName _RECCareOf_QNAME = new QName("", "REC_CareOf");
    private final static QName _ITJwPlatinum_QNAME = new QName("", "IT_Jw_Platinum");
    private final static QName _POITArtSti_QNAME = new QName("", "PO_IT_ArtSti");
    private final static QName _ORDGift_QNAME = new QName("", "ORD_Gift");
    private final static QName _ORDType_QNAME = new QName("", "ORD_Type");
    private final static QName _STPhone_QNAME = new QName("", "ST_Phone");
    private final static QName _STEmail_QNAME = new QName("", "ST_Email");
    private final static QName _ITRMMID_QNAME = new QName("", "IT_RMM_ID");
    private final static QName _INVCurrID_QNAME = new QName("", "INV_CurrID");
    private final static QName _ITSTProv_QNAME = new QName("", "IT_ST_Prov");
    private final static QName _SHIPCourierID_QNAME = new QName("", "SHIP_CourierID");
    private final static QName _ITCodiceMatricola_QNAME = new QName("", "IT_CodiceMatricola");
    private final static QName _ITINVCurrID_QNAME = new QName("", "IT_INV_CurrID");
    private final static QName _ITMacro_QNAME = new QName("", "IT_Macro");
    private final static QName _ITSTCountryCode_QNAME = new QName("", "IT_ST_CountryCode");
    private final static QName _ITJwCarats_QNAME = new QName("", "IT_Jw_Carats");
    private final static QName _INVFreight_QNAME = new QName("", "INV_Freight");
    private final static QName _ITSize_QNAME = new QName("", "IT_Size");
    private final static QName _ITColSize_QNAME = new QName("", "IT_ColSize");
    private final static QName _ITJwGold750_QNAME = new QName("", "IT_Jw_Gold750");
    private final static QName _ITSTCity_QNAME = new QName("", "IT_ST_City");
    private final static QName _ITCost_QNAME = new QName("", "IT_Cost");
    private final static QName _ORDTermOfSale_QNAME = new QName("", "ORD_TermOfSale");
    private final static QName _ITINVDisc_QNAME = new QName("", "IT_INV_Disc");
    private final static QName _FileID_QNAME = new QName("", "FileID");
    private final static QName _ITMicro_QNAME = new QName("", "IT_Micro");
    private final static QName _POITSex_QNAME = new QName("", "PO_IT_Sex");
    private final static QName _ITINVCurrISO_QNAME = new QName("", "IT_INV_CurrISO");
    private final static QName _ITSTName_QNAME = new QName("", "IT_ST_Name");
    private final static QName _RECZIP_QNAME = new QName("", "REC_ZIP");
    private final static QName _ITSTEmail_QNAME = new QName("", "IT_ST_Email");
    private final static QName _INVCurrISO_QNAME = new QName("", "INV_CurrISO");
    private final static QName _ITColor_QNAME = new QName("", "IT_Color");
    private final static QName _POITColSize_QNAME = new QName("", "PO_IT_ColSize");
    private final static QName _ITSTZIP_QNAME = new QName("", "IT_ST_ZIP");
    private final static QName _POITDisc_QNAME = new QName("", "PO_IT_Disc");
    private final static QName _SHIPReqExtraServ_QNAME = new QName("", "SHIP_ReqExtraServ");
    private final static QName _RECName_QNAME = new QName("", "REC_Name");
    private final static QName _STProv_QNAME = new QName("", "ST_Prov");
    private final static QName _ITNote_QNAME = new QName("", "IT_Note");
    private final static QName _ITINVInsurance_QNAME = new QName("", "IT_INV_Insurance");
    private final static QName _STCity_QNAME = new QName("", "ST_City");
    private final static QName _ITJwDescr_QNAME = new QName("", "IT_Jw_Descr");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.ltc.model.order.out
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Invoice }
     * 
     */
    public Invoice createInvoice() {
        return new Invoice();
    }

    /**
     * Create an instance of {@link SoldTo }
     * 
     */
    public SoldTo createSoldTo() {
        return new SoldTo();
    }

    /**
     * Create an instance of {@link Item }
     * 
     */
    public Item createItem() {
        return new Item();
    }

    /**
     * Create an instance of {@link ITSoldTo }
     * 
     */
    public ITSoldTo createITSoldTo() {
        return new ITSoldTo();
    }

    /**
     * Create an instance of {@link ITInvoice }
     * 
     */
    public ITInvoice createITInvoice() {
        return new ITInvoice();
    }

    /**
     * Create an instance of {@link Orders }
     * 
     */
    public Orders createOrders() {
        return new Orders();
    }

    /**
     * Create an instance of {@link Order }
     * 
     */
    public Order createOrder() {
        return new Order();
    }

    /**
     * Create an instance of {@link Receiver }
     * 
     */
    public Receiver createReceiver() {
        return new Receiver();
    }

    /**
     * Create an instance of {@link Shipment }
     * 
     */
    public Shipment createShipment() {
        return new Shipment();
    }

    /**
     * Create an instance of {@link Items }
     * 
     */
    public Items createItems() {
        return new Items();
    }

    /**
     * Create an instance of {@link PreOrderItems }
     * 
     */
    public PreOrderItems createPreOrderItems() {
        return new PreOrderItems();
    }

    /**
     * Create an instance of {@link PreOrderItem }
     * 
     */
    public PreOrderItem createPreOrderItem() {
        return new PreOrderItem();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Micro")
    public JAXBElement<String> createPOITMicro(String value) {
        return new JAXBElement<String>(_POITMicro_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_INV_Price")
    public JAXBElement<BigDecimal> createITINVPrice(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITINVPrice_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SHIP_ReqServLev")
    public JAXBElement<Integer> createSHIPReqServLev(Integer value) {
        return new JAXBElement<Integer>(_SHIPReqServLev_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INV_CreditDeduction")
    public JAXBElement<BigDecimal> createINVCreditDeduction(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_INVCreditDeduction_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SHIP_DeliveryDate")
    public JAXBElement<String> createSHIPDeliveryDate(String value) {
        return new JAXBElement<String>(_SHIPDeliveryDate_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SHIP_PaymentType")
    public JAXBElement<Integer> createSHIPPaymentType(Integer value) {
        return new JAXBElement<Integer>(_SHIPPaymentType_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORD_Num")
    public JAXBElement<String> createORDNum(String value) {
        return new JAXBElement<String>(_ORDNum_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_Addr1")
    public JAXBElement<String> createRECAddr1(String value) {
        return new JAXBElement<String>(_RECAddr1_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_Addr2")
    public JAXBElement<String> createRECAddr2(String value) {
        return new JAXBElement<String>(_RECAddr2_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_MadeIn")
    public JAXBElement<String> createITMadeIn(String value) {
        return new JAXBElement<String>(_ITMadeIn_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Disc")
    public JAXBElement<BigDecimal> createITDisc(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITDisc_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Jw_Gold375")
    public JAXBElement<BigDecimal> createITJwGold375(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITJwGold375_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORD_Ref")
    public JAXBElement<Integer> createORDRef(Integer value) {
        return new JAXBElement<Integer>(_ORDRef_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INV_Disc")
    public JAXBElement<BigDecimal> createINVDisc(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_INVDisc_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_Email")
    public JAXBElement<String> createRECEmail(String value) {
        return new JAXBElement<String>(_RECEmail_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Macro")
    public JAXBElement<String> createPOITMacro(String value) {
        return new JAXBElement<String>(_POITMacro_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INV_GW")
    public JAXBElement<BigDecimal> createINVGW(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_INVGW_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_Prov")
    public JAXBElement<String> createRECProv(String value) {
        return new JAXBElement<String>(_RECProv_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_CompType_ID")
    public JAXBElement<Integer> createITCompTypeID(Integer value) {
        return new JAXBElement<Integer>(_ITCompTypeID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Customs")
    public JAXBElement<String> createPOITCustoms(String value) {
        return new JAXBElement<String>(_POITCustoms_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_City")
    public JAXBElement<String> createRECCity(String value) {
        return new JAXBElement<String>(_RECCity_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Color")
    public JAXBElement<String> createPOITColor(String value) {
        return new JAXBElement<String>(_POITColor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_INV_Freight")
    public JAXBElement<BigDecimal> createITINVFreight(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITINVFreight_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORD_Date")
    public JAXBElement<XMLGregorianCalendar> createORDDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_ORDDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_Phone")
    public JAXBElement<String> createRECPhone(String value) {
        return new JAXBElement<String>(_RECPhone_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SHIP_DeliveryTime")
    public JAXBElement<String> createSHIPDeliveryTime(String value) {
        return new JAXBElement<String>(_SHIPDeliveryTime_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_ExpectedShipDate")
    public JAXBElement<XMLGregorianCalendar> createPOITExpectedShipDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_POITExpectedShipDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SHIP_From_ID")
    public JAXBElement<Integer> createSHIPFromID(Integer value) {
        return new JAXBElement<Integer>(_SHIPFromID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Jw_ItemSerialNumber")
    public JAXBElement<String> createITJwItemSerialNumber(String value) {
        return new JAXBElement<String>(_ITJwItemSerialNumber_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ArtSti")
    public JAXBElement<String> createITArtSti(String value) {
        return new JAXBElement<String>(_ITArtSti_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Marchio")
    public JAXBElement<String> createITMarchio(String value) {
        return new JAXBElement<String>(_ITMarchio_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ST_ZIP")
    public JAXBElement<String> createSTZIP(String value) {
        return new JAXBElement<String>(_STZIP_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_TransferPrice")
    public JAXBElement<BigDecimal> createITTransferPrice(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITTransferPrice_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Price")
    public JAXBElement<BigDecimal> createPOITPrice(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_POITPrice_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_TransferPriceCurrISO")
    public JAXBElement<String> createITTransferPriceCurrISO(String value) {
        return new JAXBElement<String>(_ITTransferPriceCurrISO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ST_Addr2")
    public JAXBElement<String> createITSTAddr2(String value) {
        return new JAXBElement<String>(_ITSTAddr2_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ST_Addr1")
    public JAXBElement<String> createITSTAddr1(String value) {
        return new JAXBElement<String>(_ITSTAddr1_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORD_BoxType")
    public JAXBElement<Integer> createORDBoxType(Integer value) {
        return new JAXBElement<Integer>(_ORDBoxType_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Codice10")
    public JAXBElement<String> createPOITCodice10(String value) {
        return new JAXBElement<String>(_POITCodice10_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SHIP_To_ID")
    public JAXBElement<Integer> createSHIPToID(Integer value) {
        return new JAXBElement<Integer>(_SHIPToID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORD_GW")
    public JAXBElement<String> createORDGW(String value) {
        return new JAXBElement<String>(_ORDGW_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Jw_Silver")
    public JAXBElement<BigDecimal> createITJwSilver(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITJwSilver_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_CustomerOrderID")
    public JAXBElement<Integer> createITCustomerOrderID(Integer value) {
        return new JAXBElement<Integer>(_ITCustomerOrderID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INV_CODCharge")
    public JAXBElement<BigDecimal> createINVCODCharge(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_INVCODCharge_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_Mess")
    public JAXBElement<String> createRECMess(String value) {
        return new JAXBElement<String>(_RECMess_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Marchio")
    public JAXBElement<String> createPOITMarchio(String value) {
        return new JAXBElement<String>(_POITMarchio_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_INV_CODCharge")
    public JAXBElement<BigDecimal> createITINVCODCharge(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITINVCODCharge_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Price")
    public JAXBElement<BigDecimal> createITPrice(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITPrice_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Sex")
    public JAXBElement<String> createITSex(String value) {
        return new JAXBElement<String>(_ITSex_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ST_Phone")
    public JAXBElement<String> createITSTPhone(String value) {
        return new JAXBElement<String>(_ITSTPhone_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Size")
    public JAXBElement<String> createPOITSize(String value) {
        return new JAXBElement<String>(_POITSize_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORD_Note")
    public JAXBElement<String> createORDNote(String value) {
        return new JAXBElement<String>(_ORDNote_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_CountryName")
    public JAXBElement<String> createRECCountryName(String value) {
        return new JAXBElement<String>(_RECCountryName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Comp")
    public JAXBElement<String> createPOITComp(String value) {
        return new JAXBElement<String>(_POITComp_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_ShortName")
    public JAXBElement<String> createRECShortName(String value) {
        return new JAXBElement<String>(_RECShortName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Gadget")
    public JAXBElement<Integer> createITGadget(Integer value) {
        return new JAXBElement<Integer>(_ITGadget_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Customs")
    public JAXBElement<String> createITCustoms(String value) {
        return new JAXBElement<String>(_ITCustoms_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ST_Addr1")
    public JAXBElement<String> createSTAddr1(String value) {
        return new JAXBElement<String>(_STAddr1_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Type")
    public JAXBElement<String> createITType(String value) {
        return new JAXBElement<String>(_ITType_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ST_Addr2")
    public JAXBElement<String> createSTAddr2(String value) {
        return new JAXBElement<String>(_STAddr2_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_CountryCode")
    public JAXBElement<String> createRECCountryCode(String value) {
        return new JAXBElement<String>(_RECCountryCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ST_Name")
    public JAXBElement<String> createSTName(String value) {
        return new JAXBElement<String>(_STName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INV_Insurance")
    public JAXBElement<BigDecimal> createINVInsurance(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_INVInsurance_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_ID")
    public JAXBElement<Integer> createRECID(Integer value) {
        return new JAXBElement<Integer>(_RECID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_PriorityLevel")
    public JAXBElement<Integer> createITPriorityLevel(Integer value) {
        return new JAXBElement<Integer>(_ITPriorityLevel_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ST_CountryCode")
    public JAXBElement<String> createSTCountryCode(String value) {
        return new JAXBElement<String>(_STCountryCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_MadeIn")
    public JAXBElement<String> createPOITMadeIn(String value) {
        return new JAXBElement<String>(_POITMadeIn_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_LinkPhoto")
    public JAXBElement<String> createITLinkPhoto(String value) {
        return new JAXBElement<String>(_ITLinkPhoto_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Note")
    public JAXBElement<String> createPOITNote(String value) {
        return new JAXBElement<String>(_POITNote_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Comp")
    public JAXBElement<String> createITComp(String value) {
        return new JAXBElement<String>(_ITComp_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_Phone2")
    public JAXBElement<String> createRECPhone2(String value) {
        return new JAXBElement<String>(_RECPhone2_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SHIP_Residential")
    public JAXBElement<String> createSHIPResidential(String value) {
        return new JAXBElement<String>(_SHIPResidential_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_CareOf")
    public JAXBElement<String> createRECCareOf(String value) {
        return new JAXBElement<String>(_RECCareOf_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Jw_Platinum")
    public JAXBElement<BigDecimal> createITJwPlatinum(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITJwPlatinum_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_ArtSti")
    public JAXBElement<String> createPOITArtSti(String value) {
        return new JAXBElement<String>(_POITArtSti_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORD_Gift")
    public JAXBElement<Integer> createORDGift(Integer value) {
        return new JAXBElement<Integer>(_ORDGift_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORD_Type")
    public JAXBElement<Integer> createORDType(Integer value) {
        return new JAXBElement<Integer>(_ORDType_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ST_Phone")
    public JAXBElement<String> createSTPhone(String value) {
        return new JAXBElement<String>(_STPhone_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ST_Email")
    public JAXBElement<String> createSTEmail(String value) {
        return new JAXBElement<String>(_STEmail_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_RMM_ID")
    public JAXBElement<Integer> createITRMMID(Integer value) {
        return new JAXBElement<Integer>(_ITRMMID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INV_CurrID")
    public JAXBElement<Integer> createINVCurrID(Integer value) {
        return new JAXBElement<Integer>(_INVCurrID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ST_Prov")
    public JAXBElement<String> createITSTProv(String value) {
        return new JAXBElement<String>(_ITSTProv_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SHIP_CourierID")
    public JAXBElement<Integer> createSHIPCourierID(Integer value) {
        return new JAXBElement<Integer>(_SHIPCourierID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_CodiceMatricola")
    public JAXBElement<String> createITCodiceMatricola(String value) {
        return new JAXBElement<String>(_ITCodiceMatricola_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_INV_CurrID")
    public JAXBElement<Integer> createITINVCurrID(Integer value) {
        return new JAXBElement<Integer>(_ITINVCurrID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Macro")
    public JAXBElement<String> createITMacro(String value) {
        return new JAXBElement<String>(_ITMacro_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ST_CountryCode")
    public JAXBElement<String> createITSTCountryCode(String value) {
        return new JAXBElement<String>(_ITSTCountryCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Jw_Carats")
    public JAXBElement<BigDecimal> createITJwCarats(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITJwCarats_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INV_Freight")
    public JAXBElement<BigDecimal> createINVFreight(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_INVFreight_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Size")
    public JAXBElement<String> createITSize(String value) {
        return new JAXBElement<String>(_ITSize_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ColSize")
    public JAXBElement<String> createITColSize(String value) {
        return new JAXBElement<String>(_ITColSize_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Jw_Gold750")
    public JAXBElement<BigDecimal> createITJwGold750(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITJwGold750_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ST_City")
    public JAXBElement<String> createITSTCity(String value) {
        return new JAXBElement<String>(_ITSTCity_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Cost")
    public JAXBElement<BigDecimal> createITCost(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITCost_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ORD_TermOfSale")
    public JAXBElement<String> createORDTermOfSale(String value) {
        return new JAXBElement<String>(_ORDTermOfSale_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_INV_Disc")
    public JAXBElement<BigDecimal> createITINVDisc(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITINVDisc_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "FileID")
    public JAXBElement<Integer> createFileID(Integer value) {
        return new JAXBElement<Integer>(_FileID_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Micro")
    public JAXBElement<String> createITMicro(String value) {
        return new JAXBElement<String>(_ITMicro_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Sex")
    public JAXBElement<String> createPOITSex(String value) {
        return new JAXBElement<String>(_POITSex_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_INV_CurrISO")
    public JAXBElement<String> createITINVCurrISO(String value) {
        return new JAXBElement<String>(_ITINVCurrISO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ST_Name")
    public JAXBElement<String> createITSTName(String value) {
        return new JAXBElement<String>(_ITSTName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_ZIP")
    public JAXBElement<String> createRECZIP(String value) {
        return new JAXBElement<String>(_RECZIP_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ST_Email")
    public JAXBElement<String> createITSTEmail(String value) {
        return new JAXBElement<String>(_ITSTEmail_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "INV_CurrISO")
    public JAXBElement<String> createINVCurrISO(String value) {
        return new JAXBElement<String>(_INVCurrISO_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Color")
    public JAXBElement<String> createITColor(String value) {
        return new JAXBElement<String>(_ITColor_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_ColSize")
    public JAXBElement<String> createPOITColSize(String value) {
        return new JAXBElement<String>(_POITColSize_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_ST_ZIP")
    public JAXBElement<String> createITSTZIP(String value) {
        return new JAXBElement<String>(_ITSTZIP_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "PO_IT_Disc")
    public JAXBElement<BigDecimal> createPOITDisc(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_POITDisc_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "SHIP_ReqExtraServ")
    public JAXBElement<Integer> createSHIPReqExtraServ(Integer value) {
        return new JAXBElement<Integer>(_SHIPReqExtraServ_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "REC_Name")
    public JAXBElement<String> createRECName(String value) {
        return new JAXBElement<String>(_RECName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ST_Prov")
    public JAXBElement<String> createSTProv(String value) {
        return new JAXBElement<String>(_STProv_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Note")
    public JAXBElement<String> createITNote(String value) {
        return new JAXBElement<String>(_ITNote_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_INV_Insurance")
    public JAXBElement<BigDecimal> createITINVInsurance(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_ITINVInsurance_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ST_City")
    public JAXBElement<String> createSTCity(String value) {
        return new JAXBElement<String>(_STCity_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "IT_Jw_Descr")
    public JAXBElement<String> createITJwDescr(String value) {
        return new JAXBElement<String>(_ITJwDescr_QNAME, String.class, null, value);
    }

}
