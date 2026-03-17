package tests;

import Main.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceCommandeTest {
    private DepotStock stockDisponible = reference -> 100; // Stock suffisant par défaut
    private ServiceCommande service;
    private Panier panier;
    private Article articleTest;

    @BeforeEach
    void initialiser() {
        service = new ServiceCommande(stockDisponible);
        panier = new Panier();
        articleTest = new Article("REF-001", "Cahier", 3.50);
    }

    @Test
    void commandeValideDoitRetournerUneCommande() {
        panier.ajouterArticle(articleTest, 2);
        Commande commande = service.passerCommande(panier, "CLIENT-42");
        assertNotNull(commande);
        assertEquals(7.0, commande.total(), 0.001);
    }

    @Test
    void panierVideDoitLeverIllegalStateException() {
        Panier panierVide = new Panier();
        assertThrows(IllegalStateException.class, () -> service.passerCommande(panierVide, "C1"));
    }

    @Test
    void identifiantClientNulDoitLeverException() {
        panier.ajouterArticle(articleTest, 2);
        assertThrows(IllegalArgumentException.class, () -> service.passerCommande(panier, null));
    }

    @Test
    void identifiantClientVideDoitLeverException() {
        panier.ajouterArticle(articleTest, 2);
        assertThrows(IllegalArgumentException.class, () -> service.passerCommande(panier, ""));
    }

    @Test
    void stockInsuffisantDoitLeverStockInsuffisantException() {
        // Crée un dépôt de stock avec un stock insuffisant
        DepotStock stockInsuffisant = reference -> 1; // Seule une unité disponible
        ServiceCommande serviceAvecStockInsuffisant = new ServiceCommande(stockInsuffisant);
        panier.ajouterArticle(articleTest, 2); // Demande 2 unités
        assertThrows(StockInsuffisantException.class, () -> serviceAvecStockInsuffisant.passerCommande(panier, "CLIENT-42"));
    }

    @Test
    void totalCommandeDoitCorrespondreAuTotalDuPanier() {
        panier.ajouterArticle(articleTest, 2);
        Commande commande = service.passerCommande(panier, "CLIENT-42");
        assertEquals(panier.calculerTotal(), commande.total(), 0.001);
    }
}
