# Vaatimusmäärittely - Kauppalistageneraattori

## Sovelluksen tarkoitus

Sovelluksen avulla käyttäjät voivat tallentaa reseptien ainesosaluetteloita ja generoida kauppalistoja näiden pohjalta. Sovelluksen käyttäminen vaatii rekisteröitymisen, ja tallennetut reseptit ainesosalistoineen ovat käyttäjäkohtaisia.

## Käyttäjät

Sovelluksessa on vain yksi käyttäjärooli ("normaali käyttäjä"). Mikäli sovellukseen myöhemmässä vaiheessa toteutetaan toiminnallisuus, joka mahdollistaa käyttäjäryhmien luomisen, on tässä yhteydessä järkevää harkita erilaisten käyttäjäroolien luomista (esim. käyttäjä, jolla vain lukuoikeudet jne.).

## Käyttöliittymä

Sovellus koostuu neljästä eri päänäkymästä.

![](kuvat/io.png)

Sovellus aukeaa kirjautumisnäkymään. Uusi käyttäjä voidaan myös luoda tässä samassa näkymässä. Kun sisäänkirjautuminen onnistuu, ohjelma siirtyy reseptinäkymään, jossa käyttäjä voi luoda, poistaa ja muokata reseptejään. Jokaiselle näistä toiminnoista on hieman poikkeava oma versionsa päänäkymästä, minkä lisäksi näkymälle on alussa näytettävä "neutraali" versio näkymästä. Ainesosien lisäämiselle ja ostoslistan luomiselle on molemmille omat näkymät, jotka aukeavat uusiin ikkunoihin. Sillä aikaa kun tällainen lisäikkuna on avattuna, ei taustalla yhä avoinna olevaa reseptinäkymän sisältävää ikkunaa pysty käyttämään.

## Sovelluksen perustoiminnallisuus

### Toiminnallisuus ennen kirjautumista

* käyttäjä voi luoda käyttäjätunnuksen
	* käyttäjätunnuksen täytyy olla uniikki ja vähintään kolme merkkiä
	* mikäli käyttäjätunnus on jo olemassa tai syötetty merkkijono ei kelpaa tunnukseksi, käyttäjälle näytetään tästä ilmoitus
* käyttäjä voi kirjautua sisään
	* kirjautuminen onnistuu, kun käyttäjä syöttää olemassaolevan käyttäjätunnuksen kirjautumislomakkeeseen
	* mikäli käyttäjätunnusta ei ole olemassa, käyttäjälle näytetään tästä ilmoitus

### Toiminnallisuus kirjautumisen jälkeen

* käyttäjä voi lisätä reseptin
	* reseptillä tulee olla nimi, annoskoko ja tyyppi (oletusarvona liha/kala/kasvis/makea)
	* reseptin tyyppiluokittelua voidaan muuttaa erillisen konfiguraatiotiedoston kautta
* käyttäjä voi poistaa reseptin
* käyttäjä näkee listan omista resepteistään
* käyttäjä voi lisätä resepteihin ainesosia
	* ainesosalla tulee olla nimi, määrä ja yksikkö (kg/g, l/dl tai kpl)
* käyttäjä voi poista resepteistä ainesosia
* käyttäjä voi muokata reseptien tietoja
* käyttäjä voi generoida kauppalistan
	* kauppalista perustuu käyttäjän valitsemiin resepteihin
	* reseptejä valittaessa niitä voidaan "filtteröidä" tyypin perusteella
	* kauppalistan voi tallentaa
		* kauppalistaan voi tehdä manuaalisesti muutoksia ennen tallennusta
* käyttäjä voi kirjautua ulos

## Jatkokehitysideoita

Sovellukseen voitaisiin jatkossa lisätä esimerkiksi seuraavia toiminnallisuuksia:

* reseptien haku
	* nimen perusteella
	* tietyn ainesosan perusteella
* reseptien arvostelu
* reseptin skaalaaminen sen mukaan, kuinka monelle henkilölle ruokaa ollaan tekemässä
* reseptikohtainen laskuri (kuinka monta kertaa resepti on valittu kauppalistalle)
* erillinen peruselintarvikkeet-lista, joka voidaan lisätä kauppalistalle
* kauppalistan ryhmittely
	* aakkosjärjestyksessä
	* tuoteryhmittäin
* ruokalista-näkymä, jossa voi suunnitella esim. viikon ruuat (ja tämän perusteella generoida kauppalistan)
* käyttäjäryhmät (jakavat reseptit, soveltuu esim. kotitalouksille)
* käyttäjäkohtainen salasana, joka vaaditaan kijrautumiseen
* käyttäjätunnuksen ja siihen liittyvien reseptien poistaminen
* vapaavalinteisten "tagien" lisäys resepteihin
