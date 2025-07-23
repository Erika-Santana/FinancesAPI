package model.entities;
import java.util.Map;

import enumTipos.*;

public class TotalFinancias {

    private double totalReceita;
    private double totalDespesas;
    private double saldoAtual;
    private Map<TipoCategoria, Double> despesasPorCategoria;
}
