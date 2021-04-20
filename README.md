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

### Checkstyle
Tiedoston [checkstyle.xml](https://github.com/heinapaa/ot-harjoitustyo/blob/main/Kauppalistageneraattori/checkstyle.xml) määrittelemät tarkistukset suoritetaan komennolla
```
 mvn jxr:jxr checkstyle:checkstyle
```
Kun tarkistukset on suoritettu, mahdollisiin virheilmoituksiin voi tutustua avaamalla selaimella tiedoston target/site/checkstyle.html.
