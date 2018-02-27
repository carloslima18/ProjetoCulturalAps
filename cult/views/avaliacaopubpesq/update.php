<?php

use yii\helpers\Html;

/* @var $this yii\web\View */
/* @var $model app\models\Avaliacaopubpesq */

$this->title = 'Alterar avl. pesquisador: {nameAttribute}';
$this->params['breadcrumbs'][] = ['label' => 'Avaliação pesquisador', 'url' => ['index']];
$this->params['breadcrumbs'][] = ['label' => $model->pubpesq->nome, 'url' => ['view', 'id' => $model->pubpesq->nome]];
$this->params['breadcrumbs'][] = 'Alterar';
?>
<div class="avaliacaopubpesq-update">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
