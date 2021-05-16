# Kauppalistageneraattori
Sovelluksen avulla käyttäjät voivat tallentaa reseptejä ainesosaluetteloineen, ja generoida kauppalistoja valittujen reseptien pohjalta.

Sovellus on luotu Helsingin yliopiston Tietojenkäsittelytieteen kurssin *Ohjelmistotekniikka* harjoitustyönä.

## Dokumentaatio
[Vaatimusmäärittely](https://github.com/heinapaa/ot-harjoitustyo/blob/main/dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](https://github.com/heinapaa/ot-harjoitustyo/blob/main/dokumentaatio/arkkitehtuuri.md)

[Käyttöohje](https://github.com/heinapaa/ot-harjoitustyo/blob/main/dokumentaatio/kayttoohje.md)

[Testausdokumentti](https://github.com/heinapaa/ot-harjoitustyo/blob/main/dokumentaatio/testaus.md)

[Tuntikirjanpito](https://github.com/heinapaa/ot-harjoitustyo/blob/main/dokumentaatio/tuntikirjanpito.md)

## Releaset
[Viikko 5](https://github.com/heinapaa/ot-harjoitustyo/releases/tag/viikko5)

[Viikko 6](https://github.com/heinapaa/ot-harjoitustyo/releases/tag/viikko6)

[Loppupalautus](https://github.com/heinapaa/ot-harjoitustyo/releases/tag/loppupalautus)

## Komentorivitoiminnot

### Ohjelman ajaminen
Ohjelma ajetaan komennolla
```
mvn compile exec:java -Dexec.mainClass=generator.Main
```

### Testaus
Testit suoritetaan komennolla
```
mvn test
```
Testikattavuusraportti luodaan komennolla
```
mvn jacoco:report
```

### Suoritettavan jarin generointi
Suoritettava jar-tiedosto generoidaan komennolla
```
mvn package
```
Generoinnin jälkeen tiedosto löytyy hakemistosta *target* nimellä *Kauppalistageneraattori-1.0.jar*

### JavaDoc
JavaDoc generoidaan komennolla
```
mvn javadoc:javadoc
```
JavaDocia voi tarkastella avaamalla selaimella tiedosto target/site/apidocs/index.html

### Checkstyle
Tiedoston [checkstyle.xml](https://github.com/heinapaa/ot-harjoitustyo/blob/main/Kauppalistageneraattori/checkstyle.xml) määrittelemät tarkistukset suoritetaan komennolla
```
 mvn jxr:jxr checkstyle:checkstyle
```
Kun tarkistukset on suoritettu, mahdollisiin virheilmoituksiin voi tutustua avaamalla selaimella tiedoston *target/site/checkstyle.html*.
