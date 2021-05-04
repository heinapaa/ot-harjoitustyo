# Käyttöohje

Lataa tiedosto [Kauppalistageneraattori-1.0-SNAPSHOT.jar](https://github.com/heinapaa/ot-harjoitustyo/releases/tag/viikko6).


## Konfigurointi

Ohjelma olettaa, että sen hakemistossa käynnistyshakemistossa on konfiguraatiotiedosto config.properties, joka määrittelee niiden tiedostojen nimet, mihin käyttäjät, reseptit ja ainesosat tallennetaan. Oletusarvoisesti tiedosto on seuraavanlainen:
```
userFile=users.txt
recipeFile=recipes.txt
ingredientFile=ingredients.txt
```
Lisäksi ohjelma hakee käynnistyshakemistosta tiedoston recipeTypes.properties, joka sisältää listan sallituista reseptityypeistä. Oletusarvoisesti tiedosto on seuraavanlainen:
```
types=kala,liha,kasvis,makea
```
Mikäli recipeTypes.properties -tiedostoa ei löydy, käyttää ohjelma hyväksyttyjen reseptityyppien oletusarvona yllä olevaa listaa (eli hyväksytyt tyypit ovat kala, liha, kasvis ja makea).

## Ohjelman käynnistäminen

Ohjelma käynnistetään komennolla

```
java -jar Kauppalistageneraattori-1.0-SNAPSHOT.jar
```
Huomioi että sinun tulee olla samassa hakemistossa kuin mihin jar-tiedosto on sijoitettu komennon toimimiseksi.

## Sisäänkirjautuminen

Sovellus käynnistyy kirjautumisnäkymään.

[kuvat/view_login.png]


## Resepti-näkymä

## Ainesosa-näkymä

## Kauppalista-näkymä
