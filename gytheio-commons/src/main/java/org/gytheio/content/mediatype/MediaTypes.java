package org.gytheio.content.mediatype;

/**
 * Known MIME/media type definitions.
 * <p>
 * Alfresco's MimetypeMap should utilize this interface.
 * 
 * @author Derek Hulley
 * @author Ray Gauss II
 */
public interface MediaTypes
{
    public static final String PREFIX_APPLICATION = "application/";
    
    public static final String PREFIX_AUDIO = "audio/";
    
    public static final String PREFIX_IMAGE = "image/";
    
    public static final String PREFIX_MESSAGE = "message/";
    
    public static final String PREFIX_MODEL = "model/";
    
    public static final String PREFIX_MULTIPART = "multipart/";
    
    public static final String PREFIX_TEXT = "text/";
    
    public static final String PREFIX_VIDEO = "video/";

    public static final String EXTENSION_BINARY = "bin";

    public static final String MIMETYPE_TEXT_PLAIN = "text/plain";

    public static final String MIMETYPE_TEXT_MEDIAWIKI = "text/mediawiki";

    public static final String MIMETYPE_TEXT_CSS = "text/css";

    public static final String MIMETYPE_TEXT_CSV = "text/csv";

    public static final String MIMETYPE_TEXT_JAVASCRIPT = "text/javascript";

    public static final String MIMETYPE_XML = "text/xml";

    public static final String MIMETYPE_HTML = "text/html";

    public static final String MIMETYPE_XHTML = "application/xhtml+xml";

    public static final String MIMETYPE_PDF = "application/pdf";

    public static final String MIMETYPE_JSON = "application/json";

    public static final String MIMETYPE_WORD = "application/msword";

    public static final String MIMETYPE_EXCEL = "application/vnd.ms-excel";

    public static final String MIMETYPE_BINARY = "application/octet-stream";

    public static final String MIMETYPE_PPT = "application/vnd.ms-powerpoint";

    public static final String MIMETYPE_APP_DWG = "application/dwg";

    public static final String MIMETYPE_IMG_DWG = "image/vnd.dwg";

    public static final String MIMETYPE_VIDEO_AVI = "video/x-msvideo";

    public static final String MIMETYPE_VIDEO_QUICKTIME = "video/quicktime";

    public static final String MIMETYPE_VIDEO_WMV = "video/x-ms-wmv";

    public static final String MIMETYPE_VIDEO_3GP = "video/3gpp";

    public static final String MIMETYPE_VIDEO_3GP2 = "video/3gpp2";

    // Flash
    public static final String MIMETYPE_FLASH = "application/x-shockwave-flash";

    public static final String MIMETYPE_VIDEO_FLV = "video/x-flv";

    public static final String MIMETYPE_APPLICATION_FLA = "application/x-fla";

    public static final String MIMETYPE_VIDEO_MPG = "video/mpeg";

    public static final String MIMETYPE_VIDEO_MP4 = "video/mp4";

    public static final String MIMETYPE_IMAGE_GIF = "image/gif";

    public static final String MIMETYPE_IMAGE_JPEG = "image/jpeg";

    public static final String MIMETYPE_IMAGE_RGB = "image/x-rgb";

    public static final String MIMETYPE_IMAGE_SVG = "image/svg+xml";

    public static final String MIMETYPE_IMAGE_PNG = "image/png";

    public static final String MIMETYPE_IMAGE_TIFF = "image/tiff";

    public static final String MIMETYPE_IMAGE_RAW_DNG = "image/x-raw-adobe";

    public static final String MIMETYPE_IMAGE_RAW_3FR = "image/x-raw-hasselblad";

    public static final String MIMETYPE_IMAGE_RAW_RAF = "image/x-raw-fuji";

    public static final String MIMETYPE_IMAGE_RAW_CR2 = "image/x-raw-canon";

    public static final String MIMETYPE_IMAGE_RAW_K25 = "image/x-raw-kodak";

    public static final String MIMETYPE_IMAGE_RAW_MRW = "image/x-raw-minolta";

    public static final String MIMETYPE_IMAGE_RAW_NEF = "image/x-raw-nikon";

    public static final String MIMETYPE_IMAGE_RAW_ORF = "image/x-raw-olympus";

    public static final String MIMETYPE_IMAGE_RAW_PEF = "image/x-raw-pentax";

    public static final String MIMETYPE_IMAGE_RAW_ARW = "image/x-raw-sony";

    public static final String MIMETYPE_IMAGE_RAW_X3F = "image/x-raw-sigma";

    public static final String MIMETYPE_IMAGE_RAW_RW2 = "image/x-raw-panasonic";

    public static final String MIMETYPE_IMAGE_RAW_RWL = "image/x-raw-leica";

    public static final String MIMETYPE_IMAGE_RAW_R3D = "image/x-raw-red";

    public static final String MIMETYPE_APPLICATION_EPS = "application/eps";

    public static final String MIMETYPE_JAVASCRIPT = "application/x-javascript";

    public static final String MIMETYPE_ZIP = "application/zip";

    public static final String MIMETYPE_OPENSEARCH_DESCRIPTION = "application/opensearchdescription+xml";

    public static final String MIMETYPE_ATOM = "application/atom+xml";

    public static final String MIMETYPE_RSS = "application/rss+xml";

    public static final String MIMETYPE_RFC822 = "message/rfc822";

    public static final String MIMETYPE_OUTLOOK_MSG = "application/vnd.ms-outlook";

    public static final String MIMETYPE_VISIO = "application/vnd.visio";

    // Adobe
    public static final String MIMETYPE_APPLICATION_ILLUSTRATOR = "application/illustrator";

    public static final String MIMETYPE_APPLICATION_PHOTOSHOP = "image/vnd.adobe.photoshop";

    // Open Document
    public static final String MIMETYPE_OPENDOCUMENT_TEXT = "application/vnd.oasis.opendocument.text";

    public static final String MIMETYPE_OPENDOCUMENT_TEXT_TEMPLATE = "application/vnd.oasis.opendocument.text-template";

    public static final String MIMETYPE_OPENDOCUMENT_GRAPHICS = "application/vnd.oasis.opendocument.graphics";

    public static final String MIMETYPE_OPENDOCUMENT_GRAPHICS_TEMPLATE = "application/vnd.oasis.opendocument.graphics-template";

    public static final String MIMETYPE_OPENDOCUMENT_PRESENTATION = "application/vnd.oasis.opendocument.presentation";

    public static final String MIMETYPE_OPENDOCUMENT_PRESENTATION_TEMPLATE = "application/vnd.oasis.opendocument.presentation-template";

    public static final String MIMETYPE_OPENDOCUMENT_SPREADSHEET = "application/vnd.oasis.opendocument.spreadsheet";

    public static final String MIMETYPE_OPENDOCUMENT_SPREADSHEET_TEMPLATE = "application/vnd.oasis.opendocument.spreadsheet-template";

    public static final String MIMETYPE_OPENDOCUMENT_CHART = "application/vnd.oasis.opendocument.chart";

    public static final String MIMETYPE_OPENDOCUMENT_CHART_TEMPLATE = "applicationvnd.oasis.opendocument.chart-template";

    public static final String MIMETYPE_OPENDOCUMENT_IMAGE = "application/vnd.oasis.opendocument.image";

    public static final String MIMETYPE_OPENDOCUMENT_IMAGE_TEMPLATE = "applicationvnd.oasis.opendocument.image-template";

    public static final String MIMETYPE_OPENDOCUMENT_FORMULA = "application/vnd.oasis.opendocument.formula";

    public static final String MIMETYPE_OPENDOCUMENT_FORMULA_TEMPLATE = "applicationvnd.oasis.opendocument.formula-template";

    public static final String MIMETYPE_OPENDOCUMENT_TEXT_MASTER = "application/vnd.oasis.opendocument.text-master";

    public static final String MIMETYPE_OPENDOCUMENT_TEXT_WEB = "application/vnd.oasis.opendocument.text-web";

    public static final String MIMETYPE_OPENDOCUMENT_DATABASE = "application/vnd.oasis.opendocument.database";

    // Open Office
    public static final String MIMETYPE_OPENOFFICE1_WRITER = "application/vnd.sun.xml.writer";

    public static final String MIMETYPE_OPENOFFICE1_CALC = "application/vnd.sun.xml.calc";

    public static final String MIMETYPE_OPENOFFICE1_DRAW = "application/vnd.sun.xml.draw";

    public static final String MIMETYPE_OPENOFFICE1_IMPRESS = "application/vnd.sun.xml.impress";

    // Open XML
    public static final String MIMETYPE_OPENXML_WORDPROCESSING = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String MIMETYPE_OPENXML_WORDPROCESSING_MACRO = "application/vnd.ms-word.document.macroenabled.12";
    public static final String MIMETYPE_OPENXML_WORD_TEMPLATE = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
    public static final String MIMETYPE_OPENXML_WORD_TEMPLATE_MACRO = "application/vnd.ms-word.template.macroenabled.12";
    public static final String MIMETYPE_OPENXML_SPREADSHEET = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    public static final String MIMETYPE_OPENXML_SPREADSHEET_TEMPLATE = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
    public static final String MIMETYPE_OPENXML_SPREADSHEET_MACRO = "application/vnd.ms-excel.sheet.macroenabled.12";
    public static final String MIMETYPE_OPENXML_SPREADSHEET_TEMPLATE_MACRO = "application/vnd.ms-excel.template.macroenabled.12";
    public static final String MIMETYPE_OPENXML_SPREADSHEET_ADDIN_MACRO = "application/vnd.ms-excel.addin.macroenabled.12";
    public static final String MIMETYPE_OPENXML_SPREADSHEET_BINARY_MACRO = "application/vnd.ms-excel.sheet.binary.macroenabled.12";
    public static final String MIMETYPE_OPENXML_PRESENTATION = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
    public static final String MIMETYPE_OPENXML_PRESENTATION_MACRO = "application/vnd.ms-powerpoint.presentation.macroenabled.12";
    public static final String MIMETYPE_OPENXML_PRESENTATION_SLIDESHOW = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
    public static final String MIMETYPE_OPENXML_PRESENTATION_SLIDESHOW_MACRO = "application/vnd.ms-powerpoint.slideshow.macroenabled.12";
    public static final String MIMETYPE_OPENXML_PRESENTATION_TEMPLATE = "application/vnd.openxmlformats-officedocument.presentationml.template";
    public static final String MIMETYPE_OPENXML_PRESENTATION_TEMPLATE_MACRO = "application/vnd.ms-powerpoint.template.macroenabled.12";
    public static final String MIMETYPE_OPENXML_PRESENTATION_ADDIN = "application/vnd.ms-powerpoint.addin.macroenabled.12";
    public static final String MIMETYPE_OPENXML_PRESENTATION_SLIDE = "application/vnd.openxmlformats-officedocument.presentationml.slide";
    public static final String MIMETYPE_OPENXML_PRESENTATION_SLIDE_MACRO = "application/vnd.ms-powerpoint.slide.macroenabled.12";
    // Star Office
    public static final String MIMETYPE_STAROFFICE5_DRAW = "application/vnd.stardivision.draw";

    public static final String MIMETYPE_STAROFFICE5_CALC = "application/vnd.stardivision.calc";

    public static final String MIMETYPE_STAROFFICE5_IMPRESS = "application/vnd.stardivision.impress";

    public static final String MIMETYPE_STAROFFICE5_IMPRESS_PACKED = "application/vnd.stardivision.impress-packed";

    public static final String MIMETYPE_STAROFFICE5_CHART = "application/vnd.stardivision.chart";

    public static final String MIMETYPE_STAROFFICE5_WRITER = "application/vnd.stardivision.writer";

    public static final String MIMETYPE_STAROFFICE5_WRITER_GLOBAL = "application/vnd.stardivision.writer-global";

    public static final String MIMETYPE_STAROFFICE5_MATH = "application/vnd.stardivision.math";

    // Apple iWorks
    public static final String MIMETYPE_IWORK_KEYNOTE = "application/vnd.apple.keynote";

    public static final String MIMETYPE_IWORK_NUMBERS = "application/vnd.apple.numbers";

    public static final String MIMETYPE_IWORK_PAGES = "application/vnd.apple.pages";

    // WordPerfect
    public static final String MIMETYPE_WORDPERFECT = "application/wordperfect";

    // Audio
    public static final String MIMETYPE_MP3 = "audio/mpeg";

    public static final String MIMETYPE_AUDIO_MP4 = "audio/mp4";

    public static final String MIMETYPE_VORBIS = "audio/vorbis";

    public static final String MIMETYPE_FLAC = "audio/x-flac";

    // Alfresco
    public static final String MIMETYPE_ACP = "application/acp";
    
    public String getExtension(String mediaType);

}
