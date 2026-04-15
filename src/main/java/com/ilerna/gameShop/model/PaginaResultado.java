package com.ilerna.gameShop.model;

import java.util.ArrayList;
import java.util.List;

/**
 * POJO genérico para encapsular resultados paginados.
 * Contiene la sublista de items + metadatos de paginación.
 */
public class PaginaResultado<T> {

    public static final int ITEMS_POR_PAGINA = 16;

    private List<T> items;
    private int paginaActual;
    private int totalPaginas;
    private int totalItems;

    public PaginaResultado(List<T> items, int paginaActual, int totalItems) {
        this.items = items;
        this.paginaActual = paginaActual;
        this.totalItems = totalItems;
        this.totalPaginas = (int) Math.ceil((double) totalItems / ITEMS_POR_PAGINA);
        if (this.totalPaginas == 0) this.totalPaginas = 1;
    }

    public List<T> getItems() { return items; }
    public int getPaginaActual() { return paginaActual; }
    public int getTotalPaginas() { return totalPaginas; }
    public int getTotalItems() { return totalItems; }
    public int getItemsPorPagina() { return ITEMS_POR_PAGINA; }

    public boolean isHasSiguiente() { return paginaActual < totalPaginas; }
    public boolean isHasAnterior() { return paginaActual > 1; }

    /**
     * Devuelve la lista de números de página visibles (ventana de hasta 5 páginas).
     */
    public List<Integer> getNumeros() {
        List<Integer> nums = new ArrayList<>();
        int inicio = Math.max(1, paginaActual - 2);
        int fin = Math.min(totalPaginas, paginaActual + 2);

        // Ajustar para mostrar siempre 5 si hay suficientes páginas
        if (fin - inicio < 4 && totalPaginas >= 5) {
            if (inicio == 1) {
                fin = Math.min(totalPaginas, 5);
            } else if (fin == totalPaginas) {
                inicio = Math.max(1, totalPaginas - 4);
            }
        }

        for (int i = inicio; i <= fin; i++) {
            nums.add(i);
        }
        return nums;
    }

    public static int calcularOffset(int pagina) {
        return (pagina - 1) * ITEMS_POR_PAGINA;
    }
}

