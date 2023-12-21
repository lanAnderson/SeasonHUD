# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),

## [1.7.1] - 2023-12-21

### Fixed
- Made Mixin config more compatible to prevent crashes


## [1.7.0] - 2023-12-19

### Changed
- Redid how icons are handled. Should always be lined up with the text properly now
- Edited the season icons, so they are an odd amount of pixels, so that they can be properly centered on the text
- Made a new winter icon since I couldn't get the snowflake to look right with an odd amount of pixels


## [1.6.4] - 2023-12-08

### Changed
- (1.20.2) Updated to autumn icon to the new design from SereneSeasons
- Added SereneSeasons as a required dependency on Mondrinth, now that is uploaded there as well
- Slight cleanup


## [1.6.3] - 2023-12-03

### Changed
- (1.19.2) Rewrote the options screen to allow for the 1.19.2 version to be used in 1.19.4 as well
- (1.19.x) Going forward, only one 1.19 version will be uploaded and will be marked as compatible with both versions
- Rewrote some code to allow for more common classes to be used
- Ported 1.19+ changes to 1.18.2 and made a Fabric version as well


## [1.6.2] - 2023-11-26

### Fixed
- Fixed misalignment with latest version of MapAtlases


## [1.6.1] - 2023-11-18

### Fixed
- Fixed Trinkets tag error when FabricSeason's Extra isn't installed


## [1.6.0] - 2023-11-15

### Added
- Added support for the MapAtlases mod

### Changed
- Changed Mixins to only attempt to load if the appropriate minimap mod is loaded

### Fixed
- Fixed icon alignment if Xaero's Minimap size is chunk coordinate size is wider than the minimap size
- Fixed icon alignment when using the Enlarged Minimap feature in Xaero's Minimap
- Fixed icon alignment if using OpenPartiesAndClaims, and the claim name splits into more than one line.


## [1.5.13] - 2023-11-01

### Fixed
- Adjusted math behind icon position
- (Xaero's Minimap) Fixed icon adjusting when hidden potion effects are active


## [1.5.12] - 2023-10-22

### Fixed
- Fixed a potential error with season names, if system language is not english


## [1.5.11] - 2023-10-18

### Fixed
- Fixed background transparency to match the JourneyMap setting


## [1.5.10] - 2023-10-09

### Fixed
- Fixed layering issue with newest JourneyMap


## [1.5.9] - 2023-09-30

### Added
- (1.20.2) Ported to 1.20.2

### Fixed
- Fixed season icon being misaligned when potion effects shift Xaero's Minimap


## [1.5.8] - 2023-09-19

### Fixed
- Fixed issue with the config screen when using InvMove


## [1.5.7] - 2023-09-18

### Added
- Ported changes to localization made in 1.16.5 version

### Fixed
- Fixed incorrect calendar item name for FabricSeasons Extras
- Fixed issue with the config screen when using InvMove


## [1.5.6] - 2023-09-07

### Changed
- Removed requirement of FabricSeasons Extras and made it optional


## [1.5.5] - 2023-09-04

### Added
- Added Russian translation provided by mpustovoi


## [1.5.4] - 2023-08-27

### Fixed
- Fixed Fall displaying incorrectly in the Fabric Version


## [1.5.3] - 2023-08-16

### Added
- Added JourneyMap support for Fabric

### Fixed
- Fixed label placement when using JourneyMap


## [1.5.2] - 2023-08-16

### Fixed
- Fixed Simplified Chinese


## [1.5.1] - 2023-08-05

### Added
- 1.20 update
- (Fabric) Updated to latest FabricSeasons
- (Fabric) Added FabricSeasons Extras as a required dependency, as the Calendar item was moved there


## [1.5.0] - 2023-08-02

### Added
- Added ability to change position of the season text when using Xaero's Minimap
  - Minimap Menu ('Y' by default) -> Information Settings -> Info Display Manager


## [1.4.19] - 2023-06-29

### Added

- Added 1.20.1 support

### Fixed

- Fixed crash with latest version of FTBChunks


## [1.4.18] - 2023-06-03

### Added

- Added 1.20 support
- Added an option to display the default SeasonHUD overlay if the minimap is hidden

### Fixed

- Will now disable if Journeymap is hidden


## [1.4.17] - 2023-06-02

### Fixed

- Will now disable if Xaero's Minimap is hidden


## [1.4.16] - 2023-05-24

### Added

- Added support for handling custom sub-season days


## [1.4.15] - 2023-05-16

### Fixed

- Fixed localization spacing

## [1.4.14] - 2023-04-27

### Fixed

- Overlapping with Open Parties and Claims


## [1.4.13] - 2023-04-26

### Fixed

- Fixed the HUD staying rendered while inventories are open, while using Journeymap or no minimap


## [1.4.12] - 2023-04-18

### Fixed

- Fixed background color of Journeymap labels if the theme is changed


## [1.4.11] - 2023-04-18

### Fixed

- The season HUD not showing up while using FTBChunks on a server


## [1.4.10] - 2023-04-05

### Fixed

- Fixed alignment issue with JourneyMap on the bottom half the screen

### Added

- Added config option to display above the JourneyMap minimap


## [1.4.9] - 2023-04-02

### Changed

- Slight code cleanup


## [1.4.8] - 2023-03-15

### Changed

- Re-enabled Xaero's Minimap Fairplay support
- Updated to support the latest version of Xaero's Minimap Fairplay


## [1.4.7] - 2023-03-02

### Changed

- Updated to the release version of Serene Seasons

# [1.4.6] - 2023-02-20

### Changed

- Slight code cleanup
- Xaero's Minimap Fairplay still hasn't been updated to match the regular version, so continue using [1.4.4] if using it


## [1.4.5] - 2023-02-12

### Fixed

- Fixed crash with Xaero's Minimap v23.1.0, due to the info display changes
- Temporarily disabled Fairplay functionality, until it is updated with those same changes


## [1.4.4] - 2023-02-04

### Added

- If the player is not in the overworld, the HUD will disable itself.

### Changed

- Some slight code cleanup.


## [1.4.3] - 2023-02-03

### Added

- Added a config option to show normal seasons in tropical biomes


## [1.4.2] - 2023-02-02

### Fixed

- Attempt #2 of fixing UI overlap in a claimed chunk


## [1.4.1] - 2023-02-02

### Fixed

- Fixed UI overlap if in a claimed chunk while using FTB Chunks


## [1.4.0] - 2023-01-30

### Added

- Added optional support for CuriosAPI
  - If CuriosAPI is loaded, then the Calendar item from SereneSeason can be equipped in the "Charm" slot
  - If the "Need Calendar" option is enabled, then a Calendar in the "Charm" slot will meet the requirement

## [1.3.6] - 2023-01-21

### Changed
- Some code cleanup

## [1.3.5] - 2023-01-20

### Added

- Added support for Xaero's Minimap Fair-play

## [1.3.4] - 2023-01-19

### Added

- Added an option to show the HUD only if the player has the Calendar item in their inventory

## [1.3.3] - 2023-01-11

### Added

- Added config option with presets for HUD location (when no minimap is present)
- Added button in config screen for HUD location

## [1.3.2] - 2023-01-10

### Added

- Added config options to offset the default HUD

### Changed

- Built against latest recommended Forge version

## [1.3.1] - 2023-01-06

### Added

- Added an option to disable the mod ingame.


### Changed

- Slight code cleanup

## [1.3.0] - 2022-12-29

### Added

- Added am in-game config screen and a keybind for it
  - Default key is 'H'


### Changed

- Due to some changes from 1.19.2 -> 1.19.3, I had to change to separate builds for each, rather than a unified version
- Fixed an issue with tropical season dates. Should be accurate now