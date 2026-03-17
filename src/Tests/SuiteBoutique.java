package Tests;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        ArticleTest.class,
        PanierTest.class,
        PanierReductionTest.class,
        ServiceCommandeTest.class
})
public class SuiteBoutique {
    // Pas de méthode — JUnit Platform exécute les classes déclarées ci-dessus.
}