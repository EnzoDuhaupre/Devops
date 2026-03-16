package Tests;

import Main.Article;
import Main.Panier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PanierReductionTest {

    // Test existant : un seul article, quantité 10

    @ParameterizedTest
    @CsvSource({
            " , 100.0",       // pas de code de réduction
            "REDUC10, 90.0",  // -10%
            "REDUC20, 80.0"   // -20%
    })
    void calculerTotalDoitAppliquerLaBonneReduction(
            String code, double totalAttendu) {
        // Arranger
        Panier panier = new Panier();
        Article article = new Article("REF-001", "Classeur", 10.0);
        panier.ajouterArticle(article, 10); // sous-total = 100.0
        // Agir
        if (code != null && !code.isBlank()) {
            panier.appliquerCodeReduction(code.trim());
        }
        // Affirmer
        assertEquals(totalAttendu, panier.calculerTotal(), 0.001);
    }

    @ParameterizedTest(name = "code={0} → total={1}")
    @CsvSource({
            " ,      43.00",   // pas de réduction
            "REDUC10, 38.70",  // -10 %
            "REDUC20, 34.40"   // -20 %
    })
    void calculerTotalMultiArticlesDoitAppliquerLaBonneReduction(
            String code, double totalAttendu) {
        // Arranger
        Panier panier = new Panier();
        panier.ajouterArticle(new Article("REF-010", "Cahier",   5.00), 3);
        panier.ajouterArticle(new Article("REF-011", "Stylo",    2.00), 4);
        panier.ajouterArticle(new Article("REF-012", "Classeur", 10.00), 2);

        // Agir
        if (code != null && !code.isBlank()) {
            panier.appliquerCodeReduction(code.trim());
        }

        // Affirmer
        assertEquals(totalAttendu, panier.calculerTotal(), 0.001);
    }

    // Bonus : cas invalides d'ajouterArticle() paramétrés
    //
    // Colonnes : reference | nom | prix | quantite
    //
    // Règles déclenchant IllegalArgumentException :
    //   • reference vide ou nulle
    //   • nom vide ou nul
    //   • prix négatif
    //   • quantité ≤ 0

    @ParameterizedTest(name = "ref=''{0}'' nom=''{1}'' prix={2} qte={3}")
    @CsvSource(nullValues = "NULL", value = {
            // reference invalide
            "NULL,  Stylo,  1.50,  1",   // référence nulle
            "'',   Stylo,  1.50,  1",    // référence vide

            // nom invalide
            "REF-01, NULL,  1.50,  1",   // nom nul
            "REF-01, '',   1.50,  1",    // nom vide

            // prix invalide
            "REF-01, Stylo, -0.01, 1",   // prix négatif (limite basse)
            "REF-01, Stylo, -9.99, 1",   // prix négatif (valeur quelconque)

            // quantité invalide
            "REF-01, Stylo,  1.50,  0",  // quantité nulle
            "REF-01, Stylo,  1.50, -1",  // quantité négative
            "REF-01, Stylo,  1.50, -99"  // quantité très négative
    })
    void ajouterArticleAvecParametresInvalidesDoitLeverException(
            String reference, String nom, double prix, int quantite) {
        Panier panier = new Panier();
        assertThrows(IllegalArgumentException.class, () -> {
            // La construction de l'Main.Main.Article peut déjà lever l'exception
            // (référence/nom vide, prix négatif) ; sinon c'est ajouterArticle()
            // qui la lève (quantité invalide). Les deux cas sont couverts ici.
            Article article = new Article(reference, nom, prix);
            panier.ajouterArticle(article, quantite);
        });
    }
}