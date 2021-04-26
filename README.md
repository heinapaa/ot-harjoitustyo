# Kauppalistageneraattori
Sovelluksen avulla käyttäjät voivat tallentaa reseptien ainesosaluetteloita.

Sovellus on luotu Helsingin yliopiston Tietojenkäsittelytieteen kurssin Ohjelmistotekniikka harjoitustyönä.

## Dokumentaatio
[Vaatimusmäärittely](https://github.com/heinapaa/ot-harjoitustyo/blob/main/dokumentaatio/vaatimusmaarittely.md)

[Arkkitehtuurikuvaus](https://github.com/heinapaa/ot-harjoitustyo/blob/main/dokumentaatio/arkkitehtuuri.md)

[Tuntikirjanpito](https://github.com/heinapaa/ot-harjoitustyo/blob/main/dokumentaatio/tuntikirjanpito.md)

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
Generoinnin jälkeen tiedosto löytyy hakemistosta *target* nimellä *Kauppalistageneraattori-1.0-SNAPSHOT.jar*

### Checkstyle
Tiedoston [checkstyle.xml](https://github.com/heinapaa/ot-harjoitustyo/blob/main/Kauppalistageneraattori/checkstyle.xml) määrittelemät tarkistukset suoritetaan komennolla
```
 mvn jxr:jxr checkstyle:checkstyle
```
Kun tarkistukset on suoritettu, mahdollisiin virheilmoituksiin voi tutustua avaamalla selaimella tiedoston *target/site/checkstyle.html*.
