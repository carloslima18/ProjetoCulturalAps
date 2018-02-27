<?php

use yii\helpers\Html;
use yii\grid\GridView;

/* @var $this yii\web\View */
/* @var $searchModel app\models\AvaliacaopubuserSearch */
/* @var $dataProvider yii\data\ActiveDataProvider */

$this->title = 'Avaliar pub. de usuário';
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="avaliacaopubuser-index">

    <h1><?= Html::encode($this->title) ?></h1>
    <?php // echo $this->render('_search', ['model' => $searchModel]); ?>

    <p>
        <?= Html::a('Avaliar pub. de usuário', ['create'], ['class' => 'btn btn-success']) ?>
    </p>

    <?= GridView::widget([
        'dataProvider' => $dataProvider,
        'filterModel' => $searchModel,
        'columns' => [
            ['class' => 'yii\grid\SerialColumn'],

         //   'id',
            'nota',
          //  'idpubuser',
            [
                'attribute'=>'idpubuser',				//nome do campo prorpiamente dito,
                'value'=>function($m){						//value e a função lambida, (para cada linha que vc ta processando para joga la na visualização, vc ta falando que para cada elemento chame de $m e retorne o valor (nome))
                    return $m->pubuser->nome;
                }
            ],

            ['class' => 'yii\grid\ActionColumn'],
        ],
    ]); ?>
</div>
