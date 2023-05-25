# Eksamensprojekt for Alpha Solutions

# Introduktion
### Hvad?
Projektet er et "Calculation Tool" for projekter, som har til formål at give et overblik over arbejdsprocesser - både med hensyn til penge og til tid.

### Hvorfor?
Projektet er udarbejdet som et eksamensprojekt for Alpha Solutions.

### Hvem?
Projektet er udarbejdet af Kodeklubben, DAT22D.
Medlemmer af gruppen:

 - Daniel Louis Villaceren Jappe Petersen, 17-12-2000
 - Christian Vestergaard Frandsen, 06-03-2000
 - Cihad Özdemir, 24-08-2000
 - Frederik Alexander Behrens, 13-08-1998

### BoardWave
Vi valgte navnet Board Wave, og lod os inspirere af Trello. Planen er, at man har et board med kolonner, der har cards. Man kan lave sine egne kolonner og sine egne cards. 

### Boards
Et board kan navngives, og det kan have kolonner.

### Kolonner
En kolonne kan navngives, og det har kort.

### Kort
Et kort har flere oplysninger, der bruges i vores GUI:

1. En titel
2. En beskrivelse
3. Forventet tid for gennemførsel
4. Forventet timeløn

Dette bruges til at udregne tiden, der skal sættes af til f.eks. en sprint, og hvor meget det vil koste.


# Teknologier
Vi bruger følgende teknologier:
1. Java
2. SpringBoot
3. ThymeLeaf
5. Docker - bruges til at køre appen på render
6. Render - hosting af appen
7. PlanetScale - Database hosting
8. MySQL - Database


# Installation
Du kan prøve projektet af på følgende link: [Tryk her](https://boardwave.onrender.com/)

Hvis du vil køre det selv, anbefaler vi at bruge codespace.

Hvis man ønsker at bruge sin egen database, skal man lave sin egen PlanetScale account og erstatte gældende username og password under `src/main/java/com.kodeklubben.boardwave/repositories/Repository.java` og `src/main/resources/application.properties`. Derefter, siden vi ikke bruger Foreign Keys, skal man køre `createTestData()` under `src/test/java/com.kodeklubben.boardwave/BoardControllerTest.java`, der opretter værdier med id lig med 0.

# Hvordan bruges programmet?
Vi anbefaler at bruge vores GUI, da det er mest intuitivt. 

Hvis du skal oprette et card under et board, og starter fra forsiden af, så kan du følge disse trin:

1. Tryk på `Log in` i Navigation Bar. ![landingpage](https://github.com/danieljappe/AlphaSolutionsEksamensProjekt/assets/113135538/a305e961-6ce7-4b2a-bdff-230836b5f6f5)
2. Lav en bruger og tryk `Sign up`. ![login](https://github.com/danieljappe/AlphaSolutionsEksamensProjekt/assets/113135538/c57b179b-f4cd-4815-8c2c-ac431a040234)
3. Tryk på `Create a new board`, og skriv navnet for det nye board. ![createBoard](https://github.com/danieljappe/AlphaSolutionsEksamensProjekt/assets/113135538/ac541092-f5bd-4697-81eb-ce276e844041)
4. Tryk på `View` under menuen med boards. ![View](https://github.com/danieljappe/AlphaSolutionsEksamensProjekt/assets/113135538/24fb1d11-9088-4333-89f8-4eaa89b35d7c)
5. Tryk på `Add column` i toppen. ![addColumn](https://github.com/danieljappe/AlphaSolutionsEksamensProjekt/assets/113135538/69924a4b-2ae0-4740-9b0e-eac51be545c4)
6. Tryk på `Add card` under en kolonne. ![addCard](https://github.com/danieljappe/AlphaSolutionsEksamensProjekt/assets/113135538/5eb7f612-1b82-452e-b0f5-0fa7a07afe1a)
7. Kortet er nu oprettet, og hvis man ønsker at redigere det, så kan man trykke `More` på kortet. ![more](https://github.com/danieljappe/AlphaSolutionsEksamensProjekt/assets/113135538/93fe711a-81b0-4ada-8217-c526cdceaa39)

## Endpoints
1. `/` - forside
2. `/error` - Fejlside
3. `/login-page` - Login-side og registreringsside.
4. `/boardPage={id}` - se et board ud fra dets id. Erstat `{id}` med id'et.








