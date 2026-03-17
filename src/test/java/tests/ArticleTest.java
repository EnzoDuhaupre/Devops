package tests;

import Main.Article;
import Main.Panier;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ArticleTest {

    @Test
    void creerArticleValideDoitFonctionner() {
        // Arranger & Agir
        Article article = new Article("REF-001", "Cahier", 3.50);
        // Affirmer
        assertEquals("REF-001", article.getReference());
        assertEquals("Cahier", article.getNom());
        assertEquals(3.50, article.getPrix(), 0.001);
    }

    @Test
    void modifierPrixDoitMettreAJourLePrix() {
        // Arranger
        Article article = new Article("REF-001", "Cahier", 3.50);
        // Agir
        article.setPrix(1.50);
        // Affirmer
        assertEquals(1.50, article.getPrix(), 0.001);
    }

    @Test
    void quantiteNulleDoitLeverException() {
        Panier panier = new Panier();
        Article article = new Article("REF-001", "Stylo", 1.50);
        assertThrows(IllegalArgumentException.class, () ->
                panier.ajouterArticle(article, 0));
    }

    @Test
    void articleNulDoitLeverException() {
        Panier panier = new Panier();
        assertThrows(IllegalArgumentException.class, () ->
                panier.ajouterArticle(null, 1));
    }

    @Test
    void quantiteNegativeDoitLeverException() {
        Panier panier = new Panier();
        Article article = new Article("REF-001", "Stylo", 1.50);
        assertThrows(IllegalArgumentException.class, () -> panier.ajouterArticle(article, -3));
    }


    @Test
    void setPrixNegatifDoitLeverException() {
        // Arranger
        Article article = new Article("REF-001", "Cahier", 3.50);
        assertThrows(IllegalArgumentException.class, () -> {
            article.setPrix(-5.0);
        });
    }
}
