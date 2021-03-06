<?php
/**
 * Created by PhpStorm.
 * User: carlo
 * Date: 03/03/2018
 * Time: 11:15
 */

namespace app\controllers;

use yii\rest\ActiveController;

class SendavlpubuserController extends ActiveController
{
    private $format = 'json';
    public $modelClass = 'app\models\Avaliacaopubuser';

    public function actions() {

        $actions = parent::actions();
        $actions['index']['prepareDataProvider'] = [$this, 'prepareDataProvider'];

        return $actions;
    }

    public function prepareDataProvider() {

        $searchModel = new \app\models\AvaliacaopubuserSearch();
        return $searchModel->search(\Yii::$app->request->queryParams);
    }
}
