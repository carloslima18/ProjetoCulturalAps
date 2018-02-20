<?php
namespace app\controllers;

use yii\rest\ActiveController;

class PositionController extends ActiveController
{
    private $format = 'json';
    public $modelClass = 'app\models\Publicacao';

    public function actions() {

        $actions = parent::actions();
        $actions['index']['prepareDataProvider'] = [$this, 'prepareDataProvider'];

        return $actions;
    }

    public function prepareDataProvider() {

        $searchModel = new \app\models\PublicacaoSearch();
        return $searchModel->search(\Yii::$app->request->queryParams);
    }
}
