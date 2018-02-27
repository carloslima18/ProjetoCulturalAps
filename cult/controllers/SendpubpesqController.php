<?php
/**
 * Created by PhpStorm.
 * User: carlo
 * Date: 26/02/2018
 * Time: 16:03
 */
namespace app\controllers;

use yii\rest\ActiveController;


class SendpubpesqController extends ActiveController
{
    private $format = 'json';
    public $modelClass = 'app\models\Publicacapesq';

    public function actions() {

        $actions = parent::actions();
        $actions['index']['prepareDataProvider'] = [$this, 'prepareDataProvider'];

        return $actions;
    }

    public function prepareDataProvider() {

        $searchModel = new \app\models\PublicacaopesqSearch();
        return $searchModel->search(\Yii::$app->request->queryParams);
    }
}