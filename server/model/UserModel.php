<?php
class UserModel {
    private $conn;

    public function __construct() {
        $db = new Database();
        $this->conn = $db->connect();
    }

    public function login($account, $password) {
        $query = "SELECT password
                FROM user 
                WHERE (username = '$account' OR email = '$account' OR phone = '$account') AND password = '$password'";
        $result = $this->conn->query($query);
        if ($result->num_rows > 0)
            return true;
        else
            return false;
    }

    public function signup($username, $email, $phone, $password) {
        $stmt = $this->conn->prepare("INSERT INTO user(username, email, phone, password) VALUES(?, ?, ?, ?)");
        $stmt->bind_param('ssss', $username, $email, $phone, $password);
        $result = $stmt->execute();
        $stmt->close();
        if ($result)
            return true;
        else
            return false;
    }
}