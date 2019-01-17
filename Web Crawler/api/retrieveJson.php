<?php
$title="Full Database.";
//include 'header.html.php';
include 'connect.inc.php';
session_start();

?>

<?php

if(isset($_GET['date'])){

    $currDate = ['date' => date('Y-m-d H:i:s')];

	$lastSync = $_GET['date'];
	$getProducts = $pdo->prepare("SELECT * FROM Products WHERE Date  >= :date");
	$getProducts->bindParam(':date', $lastSync);
	$getProducts->execute();
    $allProducts = array();

    foreach($getProducts as $row) {
        $product = ['code' => $row['Code'], 'title' => $row['Title'] , 'imageURL' => $row['ImageURL']];
        
        $allProducts[] = $product;

    }

    $allProducts[] = $currDate;


    header('Content-Type: application/json');

    echo json_encode($allProducts);
}
    ?>