package tests;

import Main.Article;
import Main.Panier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PanierTest {

    @Test
    void articleNulDoitLeverException() {
        Panier panier = new Panier();
        assertThrows(IllegalArgumentException.class, () -> panier.ajouterArticle(null, 1));
    }

    @Test
    void quantiteNulleDoitLeverException() {
        Panier panier = new Panier();
        Article article = new Article("REF-001", "Stylo", 1.50);
        assertThrows(IllegalArgumentException.class, () -> panier.ajouterArticle(article, 0));
    }

    @Test
    void quantiteNegativeDoitLeverException() {
        Panier panier = new Panier();
        Article article = new Article("REF-001", "Stylo", 1.50);
        assertThrows(IllegalArgumentException.class, () -> panier.ajouterArticle(article, -3));
    }

    @Test
    void codeReductionVideDoitLeverException() {
        Panier panier = new Panier();
        assertThrows(IllegalArgumentException.class, () -> panier.appliquerCodeReduction(""));
    }

    @Test
    void codeReductionNulDoitLeverException() {
        Panier panier = new Panier();
        assertThrows(IllegalArgumentException.class, () -> panier.appliquerCodeReduction(null));
    }

    // ─── TP 04 : Cas limites ──────────────────────────────────────────────────

    /**
     * Quantité minimale valide (= 1) avec un prix réel.
     * total attendu = 1 × 9.99 = 9.99
     */
    @Test
    void quantiteUneDoitEtreAcceptee() {
        // Arranger
        Panier panier = new Panier();
        Article article = new Article("REF-002", "Crayon", 9.99);

        // Agir
        panier.ajouterArticle(article, 1);

        // Affirmer
        assertEquals(9.99, panier.calculerTotal(), 0.001);
    }

    /**
     * Un article gratuit (prix = 0.0) doit être accepté sans erreur.
     * total attendu = 0.0
     */
    @Test
    void articleGratuitDoitEtreAccepte() {
        // Arranger
        Panier panier = new Panier();
        Article articleGratuit = new Article("OFFERT-01", "Stylo offert", 0.0);

        // Agir
        panier.ajouterArticle(articleGratuit, 1);

        // Affirmer
        assertEquals(0.0, panier.calculerTotal(), 0.001);
    }

    /**
     * Prix élevé (999.99) avec quantité 1 — aucun arrondi inattendu.
     * total attendu = 999.99
     */
    @Test
    void prixEleveDoitFonctionner() {
        // Arranger
        Panier panier = new Panier();
        Article article = new Article("REF-003", "Tablette", 999.99);

        // Agir
        panier.ajouterArticle(article, 1);

        // Affirmer
        assertEquals(999.99, panier.calculerTotal(), 0.001);
    }


     //Un panier avec un seul article doit renvoyer nombreArticles() == 1.

    @Test
    void panierAvecUnSeulArticleDoitFonctionner() {
        // Arranger
        Panier panier = new Panier();
        Article article = new Article("REF-004", "Règle", 2.50);

        // Agir
        panier.ajouterArticle(article, 1);

        // Affirmer
        assertEquals(1, panier.nombreArticles());
        assertFalse(panier.estVide());
    }


     //Trois articles différents : le total doit être la somme exacte des sous-totaux.

    @Test
    void plusieursArticlesDifferentsDansPanier() {
        // Arranger
        Panier panier = new Panier();
        Article crayon = new Article("REF-010", "Crayon",  1.00);
        Article cahier = new Article("REF-011", "Cahier",  3.50);
        Article stylo  = new Article("REF-012", "Stylo",   1.50);

        // Agir
        panier.ajouterArticle(crayon, 3);
        panier.ajouterArticle(cahier, 2);
        panier.ajouterArticle(stylo,  1);

        // Affirmer
        assertEquals(3,     panier.nombreArticles());
        assertEquals(11.50, panier.calculerTotal(), 0.001);
    }
}