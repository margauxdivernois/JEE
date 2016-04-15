-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Client :  127.0.0.1
-- Généré le :  Ven 19 Février 2016 à 16:29
-- Version du serveur :  5.6.17
-- Version de PHP :  5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données :  `pix`
--

-- --------------------------------------------------------

--
-- Structure de la table `album`
--

CREATE TABLE IF NOT EXISTS `album` (
  `id_album` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `a_name` varchar(250) NOT NULL,
  `a_description` text,
  `a_publicVisibility` tinyint(1) NOT NULL,
  `a_creationDate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_album`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `image`
--

CREATE TABLE IF NOT EXISTS `image` (
  `id_image` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `i_name` varchar(250) NOT NULL,
  `i_filename` varchar(250) NOT NULL,
  `i_description` text NOT NULL,
  `i_dateCapture` datetime NOT NULL,
  `i_dateUpload` datetime NOT NULL,
  `i_copyright` text NOT NULL,
  `i_latititude` double NOT NULL,
  `i_longitude` double NOT NULL,
  `i_camera` varchar(350) NOT NULL,
  `fk_album` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_image`),
  UNIQUE KEY `i_name` (`i_name`,`fk_album`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `love`
--

CREATE TABLE IF NOT EXISTS `love` (
  `id_love` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fk_image` int(10) unsigned NOT NULL,
  `fk_user` int(10) unsigned NOT NULL,
  `l_date` datetime NOT NULL,
  PRIMARY KEY (`id_love`),
  UNIQUE KEY `fk_image` (`fk_image`,`fk_user`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `permission`
--

CREATE TABLE IF NOT EXISTS `permission` (
  `id_permission` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `p_name` varchar(250) NOT NULL,
  PRIMARY KEY (`id_permission`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `u_username` varchar(250) NOT NULL,
  `u_email` varchar(254) NOT NULL,
  `u_password` varchar(32) NOT NULL,
  `u_icon` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `u_username` (`u_username`,`u_email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `user_album`
--

CREATE TABLE IF NOT EXISTS `user_album` (
  `id_user_album` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fk_user` int(10) unsigned NOT NULL,
  `fk_album` int(10) unsigned NOT NULL,
  `fk_permission` int(10) unsigned NOT NULL,
  PRIMARY KEY (`id_user_album`),
  UNIQUE KEY `fk_user` (`fk_user`,`fk_album`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
