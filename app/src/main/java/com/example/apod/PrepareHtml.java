package com.example.apod;



import java.util.ArrayList;







public class PrepareHtml  {





    public static String PrepareHtml(ArrayList<String> resultNasaValueSet) {

        // media_type
        String media_type = resultNasaValueSet.get(3);


        String resultHtml;
        resultHtml = null;


        // в зависимости от обрабатываемого контента, мы будем формировать нужную разметку
        // пока что делал только для картинок
        switch (media_type) {


            // с картинкой все просто
            case "image":
                resultHtml = "<style>\n" +
                        "   .sign {\n" +
                        "    float: center;\n" +
                        "    \n" +
                        "    padding: 7px; /* Поля внутри блока */\n" +
                        "    margin: 10px 0; /* Отступы вокруг */\n" +
                        "    \n" +
                        "   }\n" +
                        "   .sign figcaption {\n" +
                        "    margin: 0 auto 5px; /* Отступы вокруг абзаца */\n" +
                        "   }\n" +
                        "  </style>\n" +
                        " <body>\n" +
                        "  <figure class=\"sign\">\n" +
                        "   <p><img src=\"" +
                        resultNasaValueSet.get(0) +
                        "\"></p>\n" +
                        "<h1>" +
                        resultNasaValueSet.get(1) +
                        "</h1>" +
                        "   <figcaption>" +
                        resultNasaValueSet.get(2) +
                        "</figcaption>\n" +
                        "  </figure>\n" +
                        "</body>";
                break;

            // пока не знаем других типов
            default:

                resultHtml = "<h1>" + "NE UDALOS" + "<h1>";
                break;

        }


        return resultHtml;

    }

}
