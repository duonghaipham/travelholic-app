<?php
class UserController extends BaseController {
    private $user_model;

    public function login() {
        $this->model("UserModel");
        $this->user_model = new UserModel();

        if ($_SERVER['REQUEST_METHOD'] == 'POST') {
            $_POST = filter_input_array(INPUT_POST, FILTER_SANITIZE_STRING);

            $data = array(
                'account' => trim($_POST['account']),
                'password' => trim($_POST['password']),
            );

            $login_user = $this->user_model->login($data['account'], $data['password']);
            if ($login_user)
                $status = array("is_logged_in" => true);
            else
                $status = array("is_logged_in" => false);
            echo json_encode($status);
        }
    }
}