<?php
class UserModel {
    private $conn;

    public function __construct() {
        $db = new Database();
        $this->conn = $db->connect();
    }

    public function login($account, $password) {
        $query = "SELECT *
                FROM user 
                WHERE (username = '$account' OR email = '$account' OR phone = '$account') AND password = '$password'";
        $result = $this->conn->query($query);
        $data = null;
        if ($result->num_rows > 0) {
            $row = $result->fetch_assoc();
            $data = array(
                'success' => true,
                'username' => $row['username'],
                'role' => $row['role']
            );
        }
        else
            $data = array('success' => false);
        return $data;
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