package com.example.carekeeper.enums;

import lombok.Getter;

@Getter
public enum AccidentType {
    FALL(
        "Queda detectada",
        "Movimentos bruscos foram detectados. Verifique se o idoso está bem."
    ),
    IMMOBILITY(
        "Sem movimento prolongado",
        "O idoso está parado por algum tempo. Confirme se está confortável."
    ),
    OUT_OF_SAFE_ZONE(
        "Saída da área segura",
        "O idoso deixou a zona segura. Oriente-o ou acompanhe de perto."
    ),
    ABNORMAL_HEARTBEAT(
        "Frequência cardíaca fora do normal",
        "O batimento cardíaco está diferente do habitual. Observe e, se necessário, procure ajuda."
    ),
    LOW_OXYGEN(
        "Oxigenação baixa",
        "O nível de oxigênio está abaixo do recomendado. Fique atento e, se necessário, busque assistência."
    ),
    FEVER_OR_HYPOTHERMIA(
        "Temperatura corporal fora do normal",
        "A temperatura do corpo está diferente do habitual. Monitore e tome cuidado."
    ),
    CRITICAL_BLOOD_PRESSURE(
        "Pressão arterial fora do normal",
        "A pressão arterial está acima ou abaixo do recomendado. Observe sinais e cuide da segurança."
    ),
    MOVEMENT_ALONE(
        "Idoso levantou-se sozinho",
        "O idoso se levantou sozinho. Acompanhe para evitar riscos de queda."
    ),
    DARK_ENVIRONMENT(
        "Pouca luminosidade",
        "O ambiente está escuro, aumentando o risco de tropeços. Verifique a iluminação."
    );

    private final String title;
    private final String description;

    AccidentType(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
