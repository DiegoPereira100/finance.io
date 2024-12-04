package financaPessoal.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ConsomeAPI {
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key=AIzaSyBBgzQCV12qfYZCmf_kQfPdb6Q2E_HvMlI";
    private static final Pattern RESPOSTA_PATTERN = Pattern.compile("\"text\"\\s*:\\s*\"([^\"]+)\"");

    public static String fazerPergunta(String pergunta) throws
            IOException, InterruptedException {
        String jsonRequest = gerarJsonRequest(pergunta);
        String respostaJson = enviarRequisicao(jsonRequest);
        return extrairResposta(respostaJson);
    }

    private static String extrairResposta(String respostaJson) {
        StringBuilder resposta = new StringBuilder();
        for (String linha : respostaJson.lines().toList()) {
            Matcher matcher = RESPOSTA_PATTERN.matcher(linha);
            if (matcher.find()) {
                resposta.append(matcher.group(1)).append(" ");
            }
        }
        return resposta.toString().trim();
    }

    private static String enviarRequisicao(String jsonRequest) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonRequest))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString()).body();
    }

    private static String gerarJsonRequest(String pergunta) {
        String promptFormatado = "me fale o que voce esta recebendo na seguinte pergunta: "+ pergunta;
        return "{\"contents\":[{\"parts\":[{\"text\":\""+promptFormatado+"\"}]}]}";
    }
}
