<?php
$title="Full Database.";
//include 'header.html.php';
include 'connect.inc.php';
session_start();?>

<?php
  $rest_json = file_get_contents("php://input");
  $_POST = json_decode($rest_json, true);
  if (isset($_POST['productInsert'])){
    $date = date('Y-m-d H:i:s');

	$selectProduct = $pdo->prepare("SELECT * FROM Products WHERE Code = :code");

    $insertProduct = $pdo->prepare("INSERT INTO Products (Code, Title, ImageURL, Date) 
									VALUES(:code, :title, :img, :date)
									ON DUPLICATE KEY UPDATE Title = :title, ImageURL = :img, Date = :date");
	
	$selectProduct->bindParam(':code',$code);
	$insertProduct->bindParam(':code',$code);
	$insertProduct->bindParam(':title',$title);
    $insertProduct->bindParam(':img',$image);
    $insertProduct->bindParam(':date', $date);
	
	$products = $_POST['products'];
	
	print_r($products);
	
	for($i = 0; $i < count($products); $i++){
		echo("\n");
		print_r($products[$i]['code']);
		echo("\n");
		print_r($products[$i]['title']);
		echo("\n");
		print_r($products[$i]['imgURL']);
		echo("\n");
		
		$code = $products[$i]['code'];
		$title = $products[$i]['title'];
		$image = $products[$i]['imgURL'];



		$selectProduct->execute();

		if($selectProduct->rowCount() > 0){

			$rows = $selectProduct->fetch();

			$existingData = array(
				"code" => $rows['Code'],
				"title" => $rows['Title'],
				"imageURL" => $rows['ImageURL'],
			);

			$newData = array(
				"code" => $code,
				"title" => $title,
				"imageURL" => $image,
			);

			print_r($existingData);
			print_r($products[$i]);
			if($existingData != $products[$i]){
				$insertProduct->execute();
			}

		}else{

			$insertProduct->execute();

		}
		echo("Product INSERTED\n \n");
		
	}
  }

    
  else{
		echo "didnt make it here \n \n";
		print_r($_POST);

  }





?>
