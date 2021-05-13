<?php
class TourModel extends Database {
    private $conn;

    public function __construct() {
        $db = new Database();
        $this->conn = $db->connect();
    }

    public function create($creator, $tour_name, $type, $status, $departure, $destination, $during, $members, $note, $image) {
        $bitmap_data = base64_decode($image);
        $img = imagecreatefromstring($bitmap_data);
        $img_path = 'data/img/tour/' . hash('md5', $bitmap_data) . '.png';
        imagepng($img, $img_path);

        $id_query = "SELECT AUTO_INCREMENT " .
                    "FROM  INFORMATION_SCHEMA.TABLES " .
                    "WHERE TABLE_SCHEMA = 'travelholic' AND TABLE_NAME = 'tour'";
        $id = $this->conn->query($id_query)->fetch_assoc()['AUTO_INCREMENT'];

        $tour_query = "INSERT INTO tour " .
            "(creator, tour_name, type, status, departure, destination, during, note, image, created_at, updated_at)" .
            "VALUES ('$creator', '$tour_name', '$type', '$status', '$departure', '$destination', '$during', '$note', '$img_path', NOW(), NOW())";
        $this->conn->query($tour_query);

        $list_users = explode(' ', $members);
        array_push($list_users, $creator);
        foreach ($list_users as $item) {
            $member_query = "INSERT INTO member (tour_id, user) VALUES ($id, '$item')";
            $this->conn->query($member_query);
        }

        return array('success' => true, 'message' => "Tour created successfully!");
    }
}