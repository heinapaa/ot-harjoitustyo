# Vaatimusmäärittely - Kauppalistageneraattori

## Sovelluksen tarkoitus

Sovelluksen avulla käyttäjät voivat tallentaa reseptien ainesosaluetteloita ja generoida kauppalistoja näiden pohjalta. Sovelluksen käyttäminen vaatii rekisteröitymisen, ja tallennetut ainesosalistat ovat käyttäjäkohtaisia.

## Sovelluksen perustoiminnallisuus

### Toiminnallisuus ennen kirjautumista

* käyttäjä voi luoda käyttäjätunnuksen
	* käyttäjätunnuksen täytyy olla uniikki ja vähintään kolme merkkiä
* käyttäjä voi kirjautua sisään
	* kirjautuminen onnistuu, kun käyttäjä syöttää olemassaolevan käyttäjätunnuksen kirjautumislomakkeeseen
	* mikäli käyttäjätunnusta ei ole olemassa, käyttäjälle näytetään tästä ilmoitus

### Toiminnallisuus kirjautumisen jälkeen

* käyttäjä voi lisätä reseptin - ok
	* reseptillä tulee olla nimi, sekä lista vaadituista aineksista ja niiden määristä (HUOM! kesken!)
* käyttäjä voi poistaa reseptin - ok
* käyttäjä näkee listan omista resepteistään - ok
* käyttäjä voi generoida kauppalistan - perustaso ok
	* kauppalista perustuu käyttäjän valitsemiin resepteihin - ok
	* kauppalista luodaan suoraan tekstitiedostona
* käyttäjä voi kirjautua ulos

## Jatkokehitysideoita

Kun perustoiminnallisuus on toteutettu toimivasti, sovellukseen voidaan lisätä esimerkiksi seuraavia toiminnallisuuksia:

* reseptien luokittelu
	* pääraaka-aineen mukaan (liha/kana/kala/kasvis jne.)
	* tyypin mukaan (pääruoka/keitto/jälkiruoka jne.)
	* sesongin mukaan
* reseptien listaus luokittelun perusteella
* reseptien haku
	* luokittelun perusteella
	* nimen perusteella
	* tietyn ainesosan perusteella
* reseptien arvostelu
* tallennettujen reseptien muokkaaminen
* reseptin skaalaaminen sen mukaan, kuinka monelle henkilölle ruokaa ollaan tekemässä
* reseptikohtainen laskuri (kuinka monta kertaa resepti on valittu kauppalistalle)
* erillinen peruselintarvikkeet-lista, joka voidaan lisätä kauppalistalle
* kauppalistan muokkaaminen ennen tallennusta tekstitiedostoksi
* kauppalistan ryhmittely
	* aakkosjärjestyksessä
	* tuoteryhmittäin
* käyttäjäryhmät (jakavat reseptit, soveltuu esim. kotitalouksille)
* käyttäjäkohtainen salasana, joka vaaditaan kijrautumiseen
* käyttäjätunnuksen ja siihen liittyvien reseptien poistaminen
