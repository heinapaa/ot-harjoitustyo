# Testausdokumentti
## Yksikkö- ja integraatiotestaus
Yksikkö- ja integraatiotestaus on suoritettu JUnitilla.

### Sovelluslogiikka

Sovelluslogiikaa testaavat keskeisimmät integraatiotestit löytyvät luokista *IngredientServiceTest*, *RecipeServiceTest*, *ShoppingListServiceTest* ja *UserServiceTest*. Näiden testiluokkien testitapaukset simuloivat niitä keskeisiä toiminnallisuuksia, joita käyttöliittymä suorittaa luokkien *UserService*, *RecipeService*, *IngredientService* ja *ShoppingListService* avulla

Integraatiotestit käyttävät datan pysyväistallennukseen DAO-rajapintojen toteutuksia FakeUserDao, FakeRecipeDao ja FakeIngredientDao, jotka tallentavat tietoja pysyväistallennuksen sijaan keskusmuistiin.

Edellä mainittujen testiluokkien lisäksi on *InputValidator*-luokalle sekä kaikille *generator.models*-pakkauksen luokille tehty suhteellisen runsaasti (yksinkertaisia) yksikkötestejä. Osa näistä testaa päällekäisiäkin asioita integraatiotestausten kanssa. Syy tälle päällekäisyydelle on se, että testien kirjoittaminen aloitettiin jo siinä vaiheessa, kun luokkia integroivia toiminnallisuuksia ei ollut vielä juurikaan laadittu.

### DAO-luokat

SQLDao-luokkien toiminnallisuus on testattu luomalla testeissä tilapäinen tietokanta keskusmuistiin.

FileDao-luokkien toiminnallisuus on testattu luomalla testeissä tilapäinen tiedosto.

### Testauskattavuus
(kuva)
Testaamatta jäivät etenkin monet SQLException-virheen tuottavat tilanteet.
Lisäksi ohjeiden mukaisesti käyttöliittymää ei testattu automatisoiduin testein.

## Järjestelmätestaus
Järjestelmätason testaus on suoritettu manuaalisesti.

### Asennus ja konfigurointi
Sovellus on asennettu käyttöohjeen kuvaamalla tavalla ja sitä on testattu OSX- että Linux-ympäristöissä.

Testaus on suoritettu siten, että sovelluksen käynnistyshakemistossa on käyttöohjeen kuvauksen mukainen config.properties-tiedosto. Yksittäisenä config.Properties-tiedoston testinä on kokeiltu tilannetta, jossa sovelluksen tallennustapa on määritelty virheellisesti (eli ei 1 tai 2).

Sovellusta on testattu sekä tietokanta- että tiedostotallennusmoodissa, ja molemmissa tapauksissa sekä tilanteessa, jossa tallenntavat tiedostot ovat olemassa ja jossa niitä ei ole olemassa. Jälkimmäisessä tapauksessa sovellus loi tarvittavat tiedostot.

### Toiminnallisuudet
Määrittelydokumentin ja käyttöohjeen listaamat toiminnallisuudet on käyty läpi, ja ne ovat toimineet kuvatun laisesti. Kaikkien toiminnallisuuksien yhteydessä on yritetty täyttää myös virheellisiä arvoja, ja jättää arvoja täyttämättä.

## Puutteet testauksessa
- Tietokantatallennuksen testaus ei ole kovin syvällistä. JUnit-testausta ei ole suoritettu sellaisille tilanteille, joissa luokat tuottavat SQLException-virheen. Myöskään esim. kyselyiden tehokkuuteen ei ole kiinnitetty huomioita, eikä ole tutkittu sitä, miten sovellus suoriutuisi tilanteessa, jossa tallennettava tiedon määrä olisi suuri.
- Sovellusta ei ole testattu tilanteessa, joissa tallentaviin tiedostoihin ei ole tarvittavia luku- tai kirjoitusoikeuksia.
- Sovellusta ei ole testattu tilanteessa, joissa config.Properties -tiedoston syötteet ovat virheellisiä (lukuun ottamatta edellä mainittua)
- Järjestelmätestausta ei tehty Windows-ympäristössä. Tämäkin voisi olla tarpeellista.
