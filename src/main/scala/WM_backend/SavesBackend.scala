package savesbackend

import java.io._
import scala.io._
import io.circe._, io.circe.generic.auto._, io.circe.parser._, io.circe.syntax._, io.circe.generic.semiauto._

import javafx.scene.layout._
import javafx.scene._

import nodeshapes._
import nodemap._
import fighter._
import heroes._
import monsters._
import items._

object Saves {
	/* Encodage et décodage des héros */
	implicit val factionEncoder : Encoder[FactionAlignment.EnumVal] = new Encoder[FactionAlignment.EnumVal] {
		final def apply(faction : FactionAlignment.EnumVal) : Json = Json.fromString(faction.toString)
	}

	implicit val factionDecoder : Decoder[FactionAlignment.EnumVal] = new Decoder[FactionAlignment.EnumVal] {
		final def apply(c : HCursor) : Decoder.Result[FactionAlignment.EnumVal] = {
			c.as[String] match {
				case Right("Héros") => Right(FactionAlignment.Hero)
				case Right("Monstre") => Right(FactionAlignment.Monster)
				case _ => throw new Exception("Erreur : Faction inconnue")
			}
		}
	}

	implicit val fighterEncoder : Encoder[Fighter] = new Encoder[Fighter] {
		final def apply(fighter : Fighter) : Json = Json.obj(
			("fighterID", Json.fromInt(fighter.fighterID)),
			("faction", fighter.faction.asJson),
			("classIndice", Json.fromInt(fighter.classIndice)),
			("level", Json.fromInt(fighter.level)),
			("exp", Json.fromInt(fighter.exp))
		)
	}

	implicit val fighterDecoder : Decoder[Fighter] = new Decoder[Fighter] {
		final def apply(c : HCursor) : Decoder.Result[Fighter] = {
			for {
				fighterID <- c.downField("fighterID").as[Int]
				faction <- c.downField("faction").as[String]
				classIndice <- c.downField("classIndice").as[Int]
				level <- c.downField("level").as[Int]
				exp <- c.downField("exp").as[Int]
			} yield {
				var hero = Heroes.indiceToClass(classIndice, fighterID)
				hero.levelUp(level)
				hero.exp = exp
				hero
			}
		}
	}

	implicit val arrayFighterEncoder : Encoder[Array[Fighter]] = new Encoder[Array[Fighter]] {
		final def apply(fighterArray : Array[Fighter]) : Json = Json.fromValues(fighterArray.map(fighter => fighter.asJson))
	}

	implicit val arrayFighterDecoder : Decoder[Array[Fighter]] = Decoder.decodeArray

	/* Encodage et décodage de la carte du monde */
	implicit val nodeTypeEncoder : Encoder[NodeType.EnumVal] = new Encoder[NodeType.EnumVal] {
		final def apply(nodeType : NodeType.EnumVal) : Json = Json.fromString(nodeType.toString)
	}

	implicit val nodeTypeDecoder : Decoder[NodeType.EnumVal] = new Decoder[NodeType.EnumVal] {
		final def apply(c : HCursor) : Decoder.Result[NodeType.EnumVal] = {
			c.as[String] match {
				case Right("X") => Right(NodeType.FightNode)
				case Right("O") => Right(NodeType.NeutralNode)
				case Right("T") => Right(NodeType.ShopNode)
				case Right(" ") => Right(NodeType.EmptyNode)
				case _ => throw new Exception("Erreur : Type de nœud inconnu")
			}
		}
	}

	implicit val arrayNodeTypeEncoder : Encoder[Array[NodeType.EnumVal]] = new Encoder[Array[NodeType.EnumVal]] {
		final def apply(arrayNodeType : Array[NodeType.EnumVal]) : Json = Json.fromValues(arrayNodeType.map(nodeType => nodeType.asJson))
	}

	implicit val arrayNodeTypeDecoder : Decoder[Array[NodeType.EnumVal]] = Decoder.decodeArray

	implicit val internalMapEncoder : Encoder[Array[Array[NodeType.EnumVal]]] = new Encoder[Array[Array[NodeType.EnumVal]]] {
		final def apply(internalMap : Array[Array[NodeType.EnumVal]]) : Json = Json.fromValues(internalMap.map(col => col.asJson))
	}

	implicit val internalMapDecoder : Decoder[Array[Array[NodeType.EnumVal]]] = Decoder.decodeArray


	implicit val directionEncoder : Encoder[Direction.EnumVal] = new Encoder[Direction.EnumVal] {
		final def apply(dir : Direction.EnumVal) : Json = Json.fromString(dir.toString())
	}

	implicit val directionDecoder : Decoder[Direction.EnumVal] = new Decoder[Direction.EnumVal] {
		final def apply(c : HCursor) : Decoder.Result[Direction.EnumVal] = {
			c.as[String] match {
				case Right("Up") => Right(Direction.Up)
				case Right("Right") => Right(Direction.Right)
				case Right("Down") => Right(Direction.Down)
				case Right("Left") => Right(Direction.Left)
				case _ => throw new Exception("Erreur : Type de nœud inconnu")
			}
		}
	}

	implicit val boundsEncoder : Encoder[Array[List[Direction.EnumVal]]] = new Encoder[Array[List[Direction.EnumVal]]] {
		final def apply(bounds : Array[List[Direction.EnumVal]]) = Json.fromValues(bounds.map(directions => directions.asJson))
	}

	implicit val boundsDecoder : Decoder[Array[List[Direction.EnumVal]]] = new Decoder[Array[List[Direction.EnumVal]]] {
		final def apply(c : HCursor) : Decoder.Result[Array[List[Direction.EnumVal]]] = {
			c.as[Array[List[String]]] match {
				case Left(_) => throw new Exception("Erreur : Impossible de parser les aretes de la carte")
				case Right(listes) => 
					{Right(listes.map(liste => 
						liste.map(dir => {
							dir match {
								case "Up" => Direction.Up
								case "Right" => Direction.Right
								case "Down" => Direction.Down
								case "Left" => Direction.Left
							}
						})
					))}
			}
		}
	}

	implicit val nodeMapEncoder : Encoder[NodeMap] = new Encoder[NodeMap] {
		final def apply(nodeMap : NodeMap) : Json = Json.obj(
			("length", Json.fromInt(nodeMap.length)),
			("currentNode", Json.fromValues(List(Json.fromInt(nodeMap.currentNode._1), Json.fromInt(nodeMap.currentNode._2)))),
			("internalMap", nodeMap.map.asJson),
			("clearedMap", nodeMap.clearedMap.asJson),
			("bounds", nodeMap.bounds.asJson),
			("boundsAlreadyCrossed", nodeMap.boundsAlreadyCrossed.asJson),
		)
	}

	implicit val nodeMapDecoder : Decoder[NodeMap] = new Decoder[NodeMap] {
		final def apply(c : HCursor) : Decoder.Result[NodeMap] = {
			for {
				length <- c.downField("length").as[Int]
				currentNode <- c.downField("currentNode").as[List[Int]]
				internalMap <- c.downField("internalMap").as[Array[Array[NodeType.EnumVal]]]
				clearedMap <- c.downField("clearedMap").as[Array[Array[Boolean]]]
				bounds <- c.downField("bounds").as[Array[List[Direction.EnumVal]]]
				boundsAlreadyCrossed <- c.downField("boundsAlreadyCrossed").as[List[((Int, Int), Direction.EnumVal)]]
			} yield {
				var nodeMap = new NodeMap(length)
				nodeMap.currentNode = (currentNode.head, currentNode.tail.head)
				nodeMap.map = internalMap
				nodeMap.clearedMap = clearedMap
				nodeMap.bounds = bounds
				nodeMap.boundsAlreadyCrossed = boundsAlreadyCrossed
				nodeMap
			}
		}
	}

	implicit val itemEncoder : Encoder[Item] = new Encoder[Item] {
		final def apply(item : Item) : Json = Json.fromString(item.toString)
	}

	implicit val itemDecoder : Decoder[Item] = new Decoder[Item] {
		final def apply(c : HCursor) : Decoder.Result[Item] = {
			c.as[String] match {
				case Left(_) => throw new Exception("Impossible de lire les objets possédés")
				case Right("Potion de soin mineure") => Right(new MinorHealingPotion)
				case Right("Potion de soin majeure") => Right(new MajorHealingPotion)
				case Right("Potion de soin exaltée") => Right(new ExaltedHealingPotion)
				case Right("Bombe incendiaire") => Right(new FireBomb)
				case Right("Dards empoisonnés") => Right(new PoisonDart)
				case Right("Parchemin de malédiction") => Right(new CurseScroll)
				case Right("Potion de force") => Right(new StrengthPotion)
				case Right("Potion d'endurance") => Right(new ToughnessPotion)
				case Right(x) => throw new Exception("Objet " + x + " inconnu")
			}
		}
	}

	implicit val inventoryEncoder : Encoder[Inventory] = new Encoder[Inventory] {
		final def apply(inventory : Inventory) : Json = Json.obj(
			("balance", Json.fromInt(inventory.balance)),
			("items", inventory.items.asJson)
		)
	}

	implicit val inventoryDecoder : Decoder[Inventory] = new Decoder[Inventory] {
		final def apply(c : HCursor) : Decoder.Result[Inventory] = {
			for {
				balance <- c.downField("balance").as[Int]
				items <- c.downField("items").as[List[Item]]
			} yield {
				Inventory(balance, items)
			}
		}
	}

	def makeSave(nodeMap : NodeMap, heroes : Array[Fighter], inventory : Inventory) : Unit = {
		val fighterWriter = new PrintWriter(new File("src/main/resources/Save/fighters.json"))
		fighterWriter.write(heroes.asJson.toString)
		fighterWriter.close()

		val mapWriter = new PrintWriter(new File("src/main/resources/Save/map.json"))
		mapWriter.write(nodeMap.asJson.toString)
		mapWriter.close()

		val inventoryWriter = new PrintWriter(new File("src/main/resources/Save/inventory.json"))
		inventoryWriter.write(inventory.asJson.toString)
		inventoryWriter.close()
	}
		
	def loadSave() : (NodeMap, Array[Fighter], Inventory) = {
		val fighterSave = Source.fromFile("src/main/resources/Save/fighters.json").mkString
		val parseFighters = parse(fighterSave)
		val heroes = parseFighters match {
			case Left(x) => throw new Exception("Erreur : Impossible de parser la sauvegarde des combattants")
			case Right(x) => 
				decode[Array[Fighter]](fighterSave) match {
					case Left(_) => throw new Exception("Erreur : Impossible de décoder les combattants")
					case Right(x) => x
				}
		}

		val mapSave = Source.fromFile("src/main/resources/Save/map.json").mkString
		val parseMap = parse(mapSave)
		val nodeMap = parseMap match {
			case Left(x) => throw new Exception("Erreur : Impossible de parser la sauvegarde de la carte")
			case Right(x) =>
				decode[NodeMap](mapSave) match {
					case Left(x) => throw new Exception("Erreur : Impossible de décoder la carte")
					case Right(x) => x
				}
		}

		val invSave = Source.fromFile("src/main/resources/Save/inventory.json").mkString
		val parseInv = parse(mapSave)
		val inventory = parseMap match {
			case Left(x) => throw new Exception("Erreur : Impossible de parser la sauvegarde de l'inventaire")
			case Right(x) =>
				decode[Inventory](invSave) match {
					case Left(x) => throw new Exception("Erreur : Impossible de décoder la carte")
					case Right(x) => x
				}
		} 

		return (nodeMap, heroes, inventory)
	}
}