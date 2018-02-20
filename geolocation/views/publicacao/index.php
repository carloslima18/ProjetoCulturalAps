<?php

use yii\helpers\Html;
use yii\grid\GridView;

/* @var $this yii\web\View */
/* @var $searchModel app\models\PublicacaoSearch */
/* @var $dataProvider yii\data\ActiveDataProvider */

$this->title = 'Publicação';
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="publicacao-index">

    <h1><?= Html::encode($this->title) ?></h1>
    <?php // echo $this->render('_search', ['model' => $searchModel]); ?>

    <p>
        <?= Html::a('Create Publicação', ['create'], ['class' => 'btn btn-success']) ?>
    </p>
    <?= GridView::widget([
        'dataProvider' => $dataProvider,
        'filterModel' => $searchModel,
        'columns' => [
            ['class' => 'yii\grid\SerialColumn'],

            'id',
            'nome:ntext',
            'redesocial:ntext',
            'endereco:ntext',
            'contato:ntext',
            // 'atvexercida:ntext',
            // 'categoria:ntext',
            // 'latitude',
            // 'longitude',
            // 'geo_gps',
            // 'img1',
            // 'img2',
            // 'img3',
            // 'img4',
            // 'fk_user',

            ['class' => 'yii\grid\ActionColumn'],
        ],
    ]); ?>
</div>
