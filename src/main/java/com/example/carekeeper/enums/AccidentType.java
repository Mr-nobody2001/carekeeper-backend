package com.example.carekeeper.enums;

import lombok.Getter;

@Getter
public enum AccidentType {
    FALL(
        "Queda detectada",
        "{{name}} sofreu uma possível queda. Verifique imediatamente se ele(a) está bem e, se necessário, acione ajuda."
    ),
    IMMOBILITY(
        "Imobilidade prolongada",
        "{{name}} está sem movimentação há um tempo considerável. Certifique-se de que está confortável e consciente."
    ),
    OUT_OF_SAFE_ZONE(
        "Saída da área segura",
        "{{name}} deixou a área segura definida. Entre em contato e verifique se está em um local seguro."
    ),
    ABNORMAL_HEARTBEAT(
        "Alteração nos batimentos cardíacos",
        "Os batimentos cardíacos de {{name}} estão fora do padrão. Monitore e, se persistir, procure atendimento médico."
    ),
    LOW_OXYGEN(
        "Baixa oxigenação detectada",
        "O nível de oxigênio de {{name}} está abaixo do ideal. Avalie o estado de saúde e procure auxílio, se necessário."
    ),
    FEVER_OR_HYPOTHERMIA(
        "Temperatura corporal anormal",
        "A temperatura corporal de {{name}} está fora da faixa normal. Monitore e mantenha sob observação."
    ),
    CRITICAL_BLOOD_PRESSURE(
        "Pressão arterial em nível crítico",
        "A pressão arterial de {{name}} está em nível preocupante. Acompanhe de perto e busque ajuda médica, se preciso."
    ),
    MOVEMENT_ALONE(
        "Movimento independente detectado",
        "{{name}} se levantou sozinho(a). Observe para evitar quedas ou outros acidentes."
    ),
    DARK_ENVIRONMENT(
        "Ambiente com pouca iluminação",
        "O local onde {{name}} está possui pouca luz. Isso pode aumentar o risco de tropeços — verifique a iluminação."
    );

    private final String title;
    private final String description;

    AccidentType(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
