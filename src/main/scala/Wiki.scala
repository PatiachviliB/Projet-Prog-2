package wiki

import javafx._
import javafx.stage._
import javafx.scene._
import javafx.scene.image._
import javafx.scene.control._
import javafx.scene.layout._
import javafx.scene.text._
import javafx.geometry._
import javafx.scene.shape._
import javafx.scene.paint._
import javafx.scene.input._
import javafx.scene.transform._
import javafx.geometry._

import fighter._
import items._

import worldmap._
import buttons._
import menus._
import pages._

import heroesentries._
import monstersentries._
import fightmechanics._
import attackeffectsentries._

/* Classe principale du Wiki du jeu */
class Wiki(var stage : Stage, heroes : Array[Fighter]) {
	def chooseRandomPage() : Page = {
		val random = new scala.util.Random
		random.nextInt(7) match {
			case 0 => new SwordmanPage(heroes.filter(_.classIndice == 0)(0))
			case 1 => new MagicianPage(heroes.filter(_.classIndice == 1)(0))
			case 2 => new ArcherPage(heroes.filter(_.classIndice == 2)(0))
			case 3 => SlimePage
			case 4 => GoblinPage
			case 5 => SkeletonPage
			case 6 => FightMechanics
			case 7 => AttackEffectPage
		}
	}

	/* Correspond à l'écran entier */
	var root = new GridPane() {
		this.getColumnConstraints.addAll(new ColumnConstraints(300), new ColumnConstraints(2), new ColumnConstraints(2), new ColumnConstraints(1616))
	}

	/* Correspond à la barre de naviagation à gauche */
	var navigationBar = new GridPane() {
		this.getColumnConstraints.addAll(new ColumnConstraints(300))
		this.getRowConstraints.addAll(new RowConstraints(539), new RowConstraints(2), new RowConstraints(539))
		this.add(new Separator(Orientation.HORIZONTAL) {setPrefWidth(300)}, 0, 1)
	}

	/* Préparation des différents boutons qui seront utilisés */
	val swordmanButton = new WikiButton(this, 460, 780, "ÉPÉISTE", "/Fighters/swordman.png", new SwordmanPage(heroes.filter(_.classIndice == 0)(0)))
	val magicianButton = new WikiButton(this, 460, 745, "MAGICIEN", "/Fighters/magician.png", new MagicianPage(heroes.filter(_.classIndice == 1)(0)))
	val archerButton = new WikiButton(this, 460, 780, "ARCHER", "/Fighters/archer.png", new ArcherPage(heroes.filter(_.classIndice == 2)(0)))
	val heroesMenu = new MenuThreeChoices(swordmanButton, magicianButton, archerButton)

	val slimeButton = new WikiButton(this, 340, 460, "SLIME", "/Fighters/slime.png", SlimePage)
	val goblinButton = new WikiButton(this, 320, 390, "GOBELIN", "/Fighters/goblin.png", GoblinPage)
	val skeletonButton = new WikiButton(this, 345, 358, "SQUELETTE", "/Fighters/skeleton.png", SkeletonPage)
	val witchButton = new WikiButton(this, 345, 360, "WITCH", "/Fighters/witch.png", WitchPage)
	val dragonButton = new WikiButton(this, 345, 390, "DRAGON", "/Fighters/dragon.png", DragonPage)
	val skavenButton = new WikiButton(this, 345, 390, "SKAVEN", "/Fighters/skaven.png", SkavenPage)
	val ghostButton = new WikiButton(this, 345, 352, "GHOST", "/Fighters/ghost.png", GhostPage)
	val banditButton = new WikiButton(this, 345, 350, "BANDIT", "/Fighters/bandit.png", BanditPage)
	val monsterMenu = new MenuEightChoices(slimeButton, goblinButton, skeletonButton, witchButton, dragonButton, skavenButton, ghostButton, banditButton)

	val minorHealingPotionButton = new WikiButton(this, 340, 460, "POTION DE SOIN MINEURE", "/Items/minor_healing_potion.png", new ItemPage(new MinorHealingPotion))
	val majorHealingPotionButton = new WikiButton(this, 340, 460, "POTION DE SOIN MAJEURE", "/Items/major_healing_potion.png", new ItemPage(new MajorHealingPotion))
	val exaltedHealingPotionButton = new WikiButton(this, 340, 460, "POTION DE SOIN EXALTÉE", "/Items/exalted_healing_potion.png", new ItemPage(new ExaltedHealingPotion))
	val curseScrollButton = new WikiButton(this, 340, 460, "PARCHEMIN DE MALÉDICTION", "/Items/curse_scroll.png", new ItemPage(new CurseScroll))
	val fireBombButton = new WikiButton(this, 340, 400, "BOMBE INCENDIAIRE", "/Items/fire_bomb.png", new ItemPage(new FireBomb))
	val poisonDartButton = new WikiButton(this, 340, 350, "DARD EMPOISONNÉ", "/Items/poison_dart.png", new ItemPage(new PoisonDart))
	val strengthPotionButton = new WikiButton(this, 340, 350, "POTION DE FORCE", "/Items/strength_potion.png", new ItemPage(new StrengthPotion))
	val toughnessPotionButton = new WikiButton(this, 340, 350, "POTION D'ENDURANCE", "/Items/toughness_potion.png", new ItemPage(new ToughnessPotion))
	val itemsMenu = new MenuEightChoices(minorHealingPotionButton, majorHealingPotionButton, exaltedHealingPotionButton, curseScrollButton, fireBombButton, poisonDartButton, strengthPotionButton, toughnessPotionButton)

	val fightMechanicsButton = new WikiButton(this, 460, 780, "MÉCANIQUES DE COMBAT", "/Items/crossed swords.png", FightMechanics)
	val attackEffectsButton = new WikiButton(this, 460, 780, "EFFETS D'ATTAQUES", "/Items/blood drop.png", AttackEffectPage)
	val itemsButton = new WikiButton(this, 460, 780, "OBJETS", "/Items/major_healing_potion.png", itemsMenu)

	val otherMenu = new MenuThreeChoices(fightMechanicsButton, attackEffectsButton, itemsButton)

	val heroesBarButton = new BarButton(this, "HÉROS", heroesMenu)
	val monstersBarButton = new BarButton(this, "MONSTRES", monsterMenu)
	val otherBarButton = new BarButton(this, "AUTRES", otherMenu)
	val aleaButton = new BarButton(this, "ALÉATOIRE", chooseRandomPage()) {
		setOnAction(_ => updateWikiScene(chooseRandomPage()))
	}
	
	/* Correspond à la partie supérieure dans la barre de navigation */
	val quickAccess = new GridPane() {
		this.getColumnConstraints.addAll(new ColumnConstraints(50), new ColumnConstraints(200), new ColumnConstraints(50))
		this.getRowConstraints.addAll(new RowConstraints(59), new RowConstraints(60), new RowConstraints(100), new RowConstraints(100), new RowConstraints(100), new RowConstraints(100), new RowConstraints(20))
		
		this.add(new TextFlow(new Text("   Bar d'accès rapide") {
			setFill(Color.RED)
			setStyle("-fx-font-size: 18")
		}), 1, 1)

		this.add(heroesBarButton, 1, 2)
		this.add(monstersBarButton, 1, 3)
		this.add(otherBarButton, 1, 4)
		this.add(aleaButton, 1, 5)
	}
	navigationBar.add(quickAccess, 0, 0)

	/* Correspond à la partie inférieure dans la barre de navigation */
	var searchHistory : Array[Button] = Array()
	val historyAccess = new GridPane() {
		this.getColumnConstraints.addAll(new ColumnConstraints(50), new ColumnConstraints(200), new ColumnConstraints(50))
		this.getRowConstraints.addAll(new RowConstraints(59), new RowConstraints(60), new RowConstraints(100), new RowConstraints(100), new RowConstraints(100), new RowConstraints(100), new RowConstraints(20))
		this.add(new TextFlow(new Text("         Historique") {
			setFill(Color.RED)
			setStyle("-fx-font-size: 18")
		}), 1, 1)
	}
	navigationBar.add(historyAccess, 0, 2)

	val heroButton = new WikiButton(this, 460, 780, "HÉROS", "/Fighters/swordman.png", heroesMenu)
	val monsterButton = new WikiButton(this, 460, 780, "MONSTRES", "/Fighters/goblin.png", monsterMenu)
	val otherButton = new WikiButton(this, 460, 780, "AUTRES", "/Items/chest.png", otherMenu)
	val mainMenu = new MenuThreeChoices(heroButton, monsterButton, otherButton)

	/* Fonction principale permettant de mettre à jour la partie centrale du wiki pour ne pas tout recharger */
	def updateWikiScene(centralNode : Node) : Unit = {
		if (centralNode.isInstanceOf[Page]) {
			searchHistory = (new BarButton(this, centralNode.asInstanceOf[Page].name, centralNode)) +: searchHistory
			historyAccess.getChildren.clear()
			historyAccess.add(new TextFlow(new Text("         Historique") {
				setFill(Color.RED)
				setStyle("-fx-font-size: 18")
			}), 1, 1)
			for (i <- 0 to 3.min(searchHistory.size - 1)) {
				historyAccess.add(searchHistory(i), 1, i + 2)
			}
		}

		root.getChildren.clear()
		root.add(navigationBar, 0, 0)
		root.add(new Separator(Orientation.VERTICAL) {setPrefHeight(1080)}, 1, 0)
		root.add(new Separator(Orientation.VERTICAL) {setPrefHeight(1080)}, 2, 0)
		
		var mainMenuButton = new Button("Retour au menu principal") {
			setPrefSize(300, 150)
			setFocusTraversable(false)
			setOnAction(_ => updateWikiScene(mainMenu))
		}
		
		var exitButton = new Button("Retour à la carte du monde") {
			setPrefSize(300, 150)
			setFocusTraversable(false)
			setOnAction(_ => (new WorldMap).start(stage))
		}

		var centralMenu = new GridPane() {
			this.getColumnConstraints.addAll(new ColumnConstraints(400), new ColumnConstraints(300), new ColumnConstraints(16), new ColumnConstraints(300), new ColumnConstraints(400))
			this.getRowConstraints.addAll(new RowConstraints(880), new RowConstraints(200))
			this.add(centralNode, 0, 0, 5, 1)
			this.add(mainMenuButton, 1, 1)
			this.add(exitButton, 3, 1)
		}
		root.add(centralMenu, 3, 0)
	}

	updateWikiScene(mainMenu)
	var scene = new Scene(root, 1920, 1080)

	stage.setScene(scene)
}