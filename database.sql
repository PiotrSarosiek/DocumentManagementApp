-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Czas generowania: 27 Sty 2021, 20:20
-- Wersja serwera: 10.4.17-MariaDB
-- Wersja PHP: 7.2.34

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `documentdatabase`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `invoices`
--

CREATE TABLE `invoices` (
  `DocumentId` int(11) NOT NULL,
  `PartnerName` varchar(100) NOT NULL,
  `Nip` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `invoices`
--

INSERT INTO `invoices` (`DocumentId`, `PartnerName`, `Nip`) VALUES
(44, 'Piotr Sarosiek', '1234567890'),
(46, 'Krzysztof Stanowski', '1285698124'),
(51, 'Sebastian Bach', '1029374658');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `orders`
--

CREATE TABLE `orders` (
  `OrderId` int(11) NOT NULL,
  `PartnerId` int(11) DEFAULT NULL,
  `PriceNoVat` double NOT NULL,
  `PriceVat` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `orders`
--

INSERT INTO `orders` (`OrderId`, `PartnerId`, `PriceNoVat`, `PriceVat`) VALUES
(50, 21, 218, 262.85),
(51, 22, 626.72, 751.65),
(52, 24, 1398.35, 1607.45),
(53, 21, 999, 1228.77),
(55, 21, 310, 381.3),
(56, 24, 39, 47.97),
(57, 22, 994.99, 1193.35);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `order_products`
--

CREATE TABLE `order_products` (
  `Name` varchar(100) NOT NULL,
  `Quantity` int(11) NOT NULL,
  `Price` double NOT NULL,
  `Vat` double NOT NULL,
  `OrderId` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `order_products`
--

INSERT INTO `order_products` (`Name`, `Quantity`, `Price`, `Vat`, `OrderId`) VALUES
('pilka nozna', 2, 50, 0.23, 50),
('kamerka internetowa', 5, 39, 0.23, 50),
('wiertarka', 1, 59, 0.23, 51),
('rekawice bokserskie', 2, 310, 0.23, 51),
('deskorolka', 2, 258, 0.08, 52),
('rower gorski', 1, 999, 0.23, 52),
('rower gorski', 1, 999, 0.23, 53),
('rekawice bokserskie', 1, 310, 0.23, 55),
('kamerka internetowa', 1, 39, 0.23, 56),
('kamerka internetowa', 2, 39, 0.23, 57),
('buty sportowe', 4, 250, 0.23, 57);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `partners`
--

CREATE TABLE `partners` (
  `PartnerId` int(11) NOT NULL,
  `Name` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `partners`
--

INSERT INTO `partners` (`PartnerId`, `Name`) VALUES
(21, 'Piotr Sarosiek'),
(22, 'Sebastian Bach'),
(23, 'Jerzy Brzeczek'),
(24, 'Krzysztof Stanowski'),
(25, 'Piotr Sawczuk');

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `products`
--

CREATE TABLE `products` (
  `name` varchar(50) NOT NULL,
  `quantity` int(11) NOT NULL,
  `price` double NOT NULL,
  `vat` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `products`
--

INSERT INTO `products` (`name`, `quantity`, `price`, `vat`) VALUES
('rower gorski', 7, 999, 0.23),
('pilka nozna', 40, 50, 0.23),
('wiertarka', 16, 59, 0.23),
('deskorolka', 14, 258, 0.08),
('kamerka internetowa', 0, 39, 0.23),
('zapalki', 277, 1, 0.23),
('kurtka niebieska', 1, 559, 0.23),
('rekawice bokserskie', 0, 310, 0.23),
('buty sportowe', 9, 250, 0.23),
('doniczka', 4, 50, 0.23);

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `receipts`
--

CREATE TABLE `receipts` (
  `DocumentId` int(11) NOT NULL,
  `OrderId` int(11) NOT NULL,
  `Date` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Zrzut danych tabeli `receipts`
--

INSERT INTO `receipts` (`DocumentId`, `OrderId`, `Date`) VALUES
(44, 50, '2021-01-27'),
(45, 51, '2021-01-27'),
(46, 52, '2021-01-27'),
(47, 53, '2021-01-27'),
(49, 55, '2021-01-27'),
(50, 56, '2021-01-27'),
(51, 57, '2021-01-27');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indeksy dla tabeli `invoices`
--
ALTER TABLE `invoices`
  ADD KEY `DocumentId` (`DocumentId`);

--
-- Indeksy dla tabeli `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`OrderId`),
  ADD KEY `PartnerId` (`PartnerId`);

--
-- Indeksy dla tabeli `order_products`
--
ALTER TABLE `order_products`
  ADD KEY `OrderId` (`OrderId`);

--
-- Indeksy dla tabeli `partners`
--
ALTER TABLE `partners`
  ADD PRIMARY KEY (`PartnerId`);

--
-- Indeksy dla tabeli `receipts`
--
ALTER TABLE `receipts`
  ADD PRIMARY KEY (`DocumentId`),
  ADD KEY `OrderId` (`OrderId`);

--
-- AUTO_INCREMENT dla zrzuconych tabel
--

--
-- AUTO_INCREMENT dla tabeli `orders`
--
ALTER TABLE `orders`
  MODIFY `OrderId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT dla tabeli `partners`
--
ALTER TABLE `partners`
  MODIFY `PartnerId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT dla tabeli `receipts`
--
ALTER TABLE `receipts`
  MODIFY `DocumentId` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- Ograniczenia dla zrzutów tabel
--

--
-- Ograniczenia dla tabeli `invoices`
--
ALTER TABLE `invoices`
  ADD CONSTRAINT `invoices_ibfk_1` FOREIGN KEY (`DocumentId`) REFERENCES `receipts` (`DocumentId`);

--
-- Ograniczenia dla tabeli `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`PartnerId`) REFERENCES `partners` (`PartnerId`);

--
-- Ograniczenia dla tabeli `order_products`
--
ALTER TABLE `order_products`
  ADD CONSTRAINT `order_products_ibfk_1` FOREIGN KEY (`OrderId`) REFERENCES `orders` (`OrderId`);

--
-- Ograniczenia dla tabeli `receipts`
--
ALTER TABLE `receipts`
  ADD CONSTRAINT `receipts_ibfk_1` FOREIGN KEY (`OrderId`) REFERENCES `orders` (`OrderId`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
