# Arkkitehtuurikuvaus

## Rakenne

Ohjelman rakenne noudattelee kolmitasoista kerrosarkkitehtuuria, ja se koostuu kolmesta pakkauksesta:
* Pakkaus *generator.ui* sisältää JavaFX:llä toteutetun käyttäliittymän
* Pakkaus *generator.domain* sisältää sovelluslogiikan
* Pakkaus *generator.dao* sisältää tietojen pysyväistallennuksesta vastaavat luokat

## Käyttöliittymä

Käyttöliittymä sisältää neljä erillistä näkymää

* sisäänkirjautuminen-näkymä
* resepti-näkymä
* ainesosa-näkymä
* kauppalista-näkymä

Jokainen näkymä on toteutettu omana Scene-olionaan. Sisäänirjautuminen- ja resepti-näkymistä vain toinen voi olla kerrallaan näkyvänä ja ne sijoitetaan vuorollaan sovelluksen pää-stagelle. Ainesosa- ja kauppalista-näkymät puolestaan sijoitetaan erillisiin stageihin, jotka aukeavat omiin ikkunoihinsa. Käyttäliittymän toteutukseen liityvät luokat löytyvät pakkauksesta *generator.ui*. Jokainen näkymä on luotu omassa luokassaan. Lisäksi pakkauksessa on luokka *Router*, joka vastaa näkymien kutsumisesta ja tarvittaessa ikkunoiden avaamisesta ja sulkemisesta.

Näkymien luomisesta vastaavat luokat toteuttavat rajapinnan *View* jossa on yksi metodi, *create()*. Rajapinta on luotu käyttöliittymän jatkokehitystä silmällä pitäen.

Käyttöliittymä on pyritty eriyttämään sovelluslogiikasta. Sen luokat ainoastaan kutsuvat sovelluslogiikan toteuttavien olioiden metodeja sopivilla parametreilla.

## Sovelluslogiikka

Sovelluksen loogisen datamallin muodostavat seuraavat kolme luokkaa:

* *User*, joka kuvaa käyttäjiä
* *Recipe*, joka kuvaa käyttäjälle kuuluvaa reseptiä
* *Ingredient*, joka kuvaa reseptiin liittyvää ainesosaa

![](kuvat/1.png)

Toiminnallisista kokonaisuuksista vastaavat luokat *UserService*, *RecipeService*, *IngredientService*, *ShoppingListService* ja *InputValidator*. Jokaisesta luokasta luodaan vain yksi olio. Luokat on kuvattu tarkemmin alla olevassa taulukossa

|Luokka|Tehtävä|Esimerki metodeista|
|---|---|---|
|UserService|Käyttäjien käsittely|logIn(User user)|
|RecipeService|Reseptien käsittely|removeRecipe(Recipe recipe, User user)|
|IngredientService|Ainesosien käsittely|addIngredient(Recipe recipe, String ingredientName, String ingredientUnit, String ingredientAmount)|
|ShoppingListService|Ostoslistan luominen|createShoppingList(List<Recipe> recipes)|
|InputValidator|Syötteiden validointi|isValidIngredientUnit(String input)|

Sovelluslogiikasta vastaavat luokat pääsevät käsiksi käyttäjien, reseptien ja ainesosien tietoihin pakkauksessa *generator.dao* sjijaitsevien, rajapinnat UserDao, RecipeDao ja IngredientDao toteuttavien luokkien kautta. Tarvittavien luokkien toteutukset injektoidaan sovelluslogiikkaa toteuttaville olioille konstruktorikutsun yhteydessä. Service-olioihin injektoidaan lisäksi InputValidator-luokan toteutus.

Alla vanhentunut luokkakaavio, jossa kaikki sovelluslogiikan toiminnot löytyivät vielä RecipeService-luokasta. Kaavio tullaan päivittämään.

![](kuvat/2.png)

## Pysyväistallennus
Pakkauksen generator.dao luokat *FileUserDao*, *FileRecipeDao* ja *FileIngredientDao* huolehtivat tietojen tallettamisesta tiedostoihin.

Luokat noudattavat Data Access Object -suunnittelumallia, ja ne on eristetty rajapintojen *UserDao*, *RecipeDao* ja *IngredientDao* taakse. Koksa sovelluslogiikka ei käytä luokkia suoraan, ne on mahdollista korvata uusilla vaihtoehtoisilla toteutuksilla mikäli datan talletustapaa halutaan vaihtaa (esim. tietokantaan).

Myös sovelluslogiikan testauksessa hyödynnetään DAO-suunnittelumallia. Testeissä käytetään olioita *FakeUserDao*, *FakeRecipeDao* ja *FakeIngredientDao*, jotka tekevät väliaikaisia tallennuksia keskusmuistiin pysyväistallentamisen sijaan.

### Tiedostot

Sovellus tallentaa käyttäjien, reseptien ja ainesosien tiedot tekstitiedostoihin. Sovelluksen *resources*-kansiosta löytyvä *config.properties*-tiedosto määrittelee tekstitiedostojen nimet.

Sovellus tallentaa käyttäjät seuraavassa muodossa
```
heinapaa
lettu
uusi2
```
eli listana.

Sovellus tallentaa reseptit seuraavassa muodossa
```
3;kasvisresepti;10;kasvis;heinapaa
8;testi;10;liha;lettu
```
eli ensin reseptin tunniste, sitten otsikko, sitten annoskoko, sitten tyyppi ja lopuksi käyttäjä, johon resepti liittyy. Tiedot erotetaan toisistaan tuplapuolipisteellä (";;").

Sovellus tallentaa ainesosat seuraavassa muodossa
```
1;sokeri;5.0;dl;5
5;sokeri;1.0;l;2
```
eli ensin reseptin tunniste, sitten ainesosan otsikko, sitten määrä, sitten yksikkö, ja lopuksi ainesosan tunniste. Tiedot erotetaan toisistaan tuplapuolipisteellä (";;").

## Päätoiminnallisuudet

Tullaan täydentämään

![](kuvat/login.png)

## Heikkoidet

Tullaan täydentämään
