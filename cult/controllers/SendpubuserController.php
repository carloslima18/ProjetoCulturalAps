<?php
/**
 * Created by PhpStorm.
 * User: carlo
 * Date: 26/02/2018
 * Time: 16:00
 */

namespace app\controllers;
use yii\rest\ActiveController;


class SendpubuserController extends ActiveController
{
    private $format = 'json';
    public $modelClass = 'app\models\Publicacaouser';

    public function actions() {

        $actions = parent::actions();
        $actions['index']['prepareDataProvider'] = [$this, 'prepareDataProvider'];

        return $actions;
    }


    public function prepareDataProvider() {

        $searchModel = new \app\models\PublicacaouserSearch();
        return $searchModel->search(\Yii::$app->request->queryParams);
    }
}