# NBA_DATABASE_PROJECT FOR AGH BAZY DANYCH 1

## Instalation
To install all of the required libraries we need to run pip install -r req.txt.

## Interfejs Gościa
Po włączeniu aplikacji wyświetla się okno główne gościa wygląda ono następująco:

Z tego poziomu możemy, lecz nie musimy się zalogować (bądź zarejestrować). Możemy również przejść do jednej z 4 opcji:
- Mecze
- Ligowi Liderzy
- Wykres
- Tabele
Wymienione opcje będą opisane niżej.

## Okno Logowania
Po naciśnięciu przycisku Zaloguj Się otworzy nam się okno Logowania:
# Image
Na samej górze okna mamy przycisk umożliwiający rejestrację do systemu, która będzie opisywana niżej.
Następnie mamy dwa pola tekstowe:
- Login
- Hasło
Z czego hasło jest polem tekstowym ukrytym (zamiast tekstu widać gwiazdki).
Po kliknięciu przycisku zaloguj następuję w

## Interfejs Użytkownika
Po zalogowaniu do systemu widzimy że jesteśmy zalogowani jako: login (Logged in as: login). Z tej pozycji możemy się wylogować naciskając przycisk Wyloguj się. Po nacisnięciu tego przycisku spowrotem wyświetla się Okno Gościa.
W Interfejsie Użytkownika widnieją te same opcje, które widniały w Interfejsie Gościa.

## Interfejs Admina
Po zalogowaniu do systemu jako admin - (Trzeba przypisać manualnie z pozycji bazy danych), widzimy prakycznie to samo co przy zalogowaniu jako użytkownik, chociaż widac że jesteśmy adminem, oraz dostępna jest jeszcze jedna dodatkowa opcja:
- Dodaj mecze
Opcja ta również będzie opisana niżej.

## Okno Rejestracji
W oknie rejestracji możemy znaleźć przycisk Logowania oraz trzy pola tekstowe:
- Login
- Hasło
- Powtórz Hasło
Hasło oraz Powtórz Hasło są polami tekstowymi ukrytymi.
Po nacisnięciu przycisku Zarejestruj się następuje utworzenie nowego użytkowanika (jeśli login jest unikalny).

# Funkcjonalności

## Okno Mecze
Po otworzeniu okna mecze z dowolnego poziomu użytkownika (Gość, Użytkownik, Admin) widzimy trzy listy rozwijane:
- Dzień
- Miesiąc
- Rok
Które służą do wybrania daty dnia, z którego chcemy wyświetlić mecze.
Po naciśnięciu przycisku Szukaj, otrzymujemy listę meczów na dany dzień (Drużyny, oraz wyniki meczów). Możemy nacisnąć na dowolny z meczów aby wejść do Okna Box Score danego meczu.

## Okno Box Score
W oknie Box Score dla danego meczu widzimy drużyny oraz wynik meczu, a poniżej dwa przyciski odpowiadające drużynom biorącym udział w meczu.
Naciśnięcie jednego z nich powoduje wyswietlenie tabeli wynikowej dla tej drużyny (tzw. box score).
Domyślnie wyświetlana jest tabela wynikowa dla drużyny gospodarzy.

## Okno Ligowi Liderzy

